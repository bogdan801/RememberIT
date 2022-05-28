package com.bogdan801.rememberit.presentation.windows.notes

import androidx.compose.runtime.*
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.di.BaseApplication
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.data.datastore.readStringFromDataStore
import com.bogdan801.rememberit.data.datastore.saveStringToDataStore
import com.bogdan801.rememberit.data.mapper.toNote
import com.bogdan801.rememberit.data.mapper.toTask
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.model.Task
import com.bogdan801.rememberit.domain.notifications.cancelNotification
import com.bogdan801.rememberit.domain.notifications.scheduleNotification
import com.bogdan801.rememberit.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

/**
 * Це клас [NotesViewModel], являє собою ViewModel для головного вікна
 * @constructor в конструктор передаються репозиторій та BaseApplication
 * @param repository репозиторій, клас через методи якого надається доступ до локальної бази даних
 * @param application базовий клас додатку
 * @property searchBarTextState стан вмісту строки пошуку
 * @property searchPlaceholderState стан заповнювача пустої строки пошуку
 * @property allNotesState стан списку всіх нотаток
 * @property foundNotesState стан списку останніх знайдених нотаток
 * @property allTasksState стан списку всіх завдань
 * @property foundTasksState стан списку останніх знайдених завдань
 */
@HiltViewModel
class NotesViewModel
@Inject
constructor(
    private val repository: Repository,
    private val application: BaseApplication
) : ViewModel() {
    //searchbar states
    private var _searchBarTextState = mutableStateOf("")
    private var _searchPlaceholderState = mutableStateOf(application.getString(R.string.search_notes))
    var searchBarTextState: State<String> = _searchBarTextState
    var searchPlaceholderState: State<String> = _searchPlaceholderState

    /**
     * Метод зміни значення тексту поля строки пошуку[searchBarTextState]
     * @param newText новий текст пошукового поля
     * @param pageState індекс відкритої сторінки
     */
    fun searchBarValueChanged(newText: String, pageState: Int){
        _searchBarTextState.value = newText
        if(pageState == 0){
            viewModelScope.launch {
                _foundNotesState.value = repository.searchNotes(newText).map { it.toNote() }
            }
        }

        if(pageState == 1){
            viewModelScope.launch {
                _foundTasksState.value = repository.searchTasks(newText).map { it.toTask() }
            }
        }
    }

    /**
     * Метод для встановлення тексту заповнювача [searchPlaceholderState] пустої строки пошушу
     * @param pageState індекс відкритої сторінки
     */
    fun setPlaceholder(pageState: Int){
        _searchPlaceholderState.value = if (pageState == 0) application.getString(R.string.search_notes) else application.getString(R.string.search_tasks)
    }

    //notes states
    private val _allNotesState = mutableStateOf(listOf<Note>())
    private val _foundNotesState = mutableStateOf(listOf<Note>())
    var allNotesState: State<List<Note>> = _allNotesState
    var foundNotesState: State<List<Note>> = _foundNotesState
    private lateinit var lastDeletedNote: Note
    /**
     * Метод натиску кнопки видалення нотатки
     * @param id індекс обраної нотатки
     */
    fun noteDeleteClick(id :Int){
        viewModelScope.launch {
            lastDeletedNote = repository.getNoteById(id).toNote()
            repository.deleteNote(id)

            if(_searchBarTextState.value.isNotBlank()){
                _foundNotesState.value = repository.searchNotes(_searchBarTextState.value).map { it.toNote() }
            }
        }


    }

    /**
     * Method for recovering last deleted note
     */
    fun recoverNoteClick(){
        viewModelScope.launch {
            repository.insertNote(lastDeletedNote)

            if(_searchBarTextState.value.isNotBlank()){
                _foundNotesState.value = repository.searchNotes(_searchBarTextState.value).map { it.toNote() }
            }
        }
    }

    //tasks states
    private val _allTasksState = mutableStateOf(listOf<Task>())
    private val _foundTasksState = mutableStateOf(listOf<Task>())
    var allTasksState: State<List<Task>> = _allTasksState
    var foundTasksState: State<List<Task>> = _foundTasksState
    private lateinit var lastDeletedTask: Task

    /**
     * Метод натиску кнопки видалення завдання
     * @param id індекс обраного завдання
     */
    fun taskDeleteClick(id :Int){
        viewModelScope.launch {
            lastDeletedTask = repository.getTaskById(id).toTask()
            repository.deleteTask(id)

            if(_searchBarTextState.value.isNotBlank()){
                _foundTasksState.value = repository.searchTasks(_searchBarTextState.value).map { it.toTask() }
            }
        }


    }

    /**
     * Method for recovering last deleted task
     */
    fun recoverTaskClick(){
        viewModelScope.launch {
            repository.insertTask(lastDeletedTask)

            if(_searchBarTextState.value.isNotBlank()){
                _foundTasksState.value = repository.searchTasks(_searchBarTextState.value).map { it.toTask() }
            }
        }
    }

    /**
     * Метод натиску галочки виконання завдання
     * @param id індекс обраного завдання
     * @param status статус виконання завдання
     */
    fun taskCheckedChanged(id :Int, status: Boolean){
        viewModelScope.launch {
            repository.updateTaskStatus(id, status)
        }

        if(_searchBarTextState.value.isNotBlank()){
            viewModelScope.launch {
                _foundTasksState.value = repository.searchTasks(_searchBarTextState.value).map { it.toTask() }
            }
        }
    }

    /**
     * Метод для управління сповіщеннями для завдань
     * @param tasks список завдань
     */
    private suspend fun manageTaskNotifications(tasks: List<Task>){
        //cancel all previous alarms
        val alarmsString = application.readStringFromDataStore("alarms")
        var alarmsIDs = listOf<Int>()
        if(alarmsString!=null){
            if(alarmsString.isNotBlank()){
                alarmsIDs = alarmsString
                    .split(" ")
                    .filter{it.isNotBlank()}
                    .map {it.toInt()}
            }
        }

        alarmsIDs.forEach {
            application.cancelNotification(it)
        }

        //set new alarms
        var newAlarmsString = ""
        tasks.filter { task ->
            !task.isChecked && (task.dueTo> Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
        }.forEach { task ->
            newAlarmsString += task.id.toString() + " "
            application.scheduleNotification(
                notificationID = task.id,
                title = application.getString(R.string.the_task_is_due),
                message = task.contents,
                time = task.dueTo.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            )
        }

        //save new alarms id list as string in Datastore
        application.saveStringToDataStore("alarms", newAlarmsString)
    }

    //on viewmodel initialization
    /**
     * Блок ініціалізації ViewModel
     */
    init {
        viewModelScope.launch {
            repository.getNotes().collect{ dbList ->
                _allNotesState.value = dbList.map { noteEntity->
                    noteEntity.toNote()
                }
            }
        }

        viewModelScope.launch {
            repository.getTasks().collect{ dbList ->
                val tasks = dbList.map { taskEntity->
                    taskEntity.toTask()
                }
                _allTasksState.value = tasks
                manageTaskNotifications(tasks)
            }
        }
    }
}
