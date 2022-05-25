package com.bogdan801.rememberit.presentation.windows.addtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.data.mapper.toTask
import com.bogdan801.rememberit.domain.model.Task
import com.bogdan801.rememberit.domain.repository.Repository
import com.bogdan801.rememberit.presentation.windows.util.UndoRedoStack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*
import javax.inject.Inject

/**
 * Це клас [AddTaskViewModel], являє собою ViewModel для вікна додавання/редагування завдання
 * @constructor в конструктор передається репозиторій
 * @param repository репозиторій, клас через методи якого надається доступ до локальної бази даних
 * @property editID індекс завдання для редагування, якщо він рівний -1, то буде створене нове завдання, якщо ж ні,
 * то при збереженні буде відредаговане завдання під цим індексом
 * @property contentsUndoStack стак збереження і управління змінами вмісту завдання(undo/redo функціонал)
 * @property undoShowState стан активності кнопки Undo
 * @property redoShowState стан активності кнопки Redo
 * @property tasksContentsTextState стан тексту вмісту завдання
 * @property dueToDateTimeState стан дати і часу завдання
 */
@HiltViewModel
class AddTaskViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private var taskID: Int = repository.getMaxTaskId()?.plus(1) ?: 1
    //private var editID = -1

    /**
     * Метод ініціалізації ViewModel для редагування завдання
     * @param editId id завдання для редагування
     */
    fun initEditId(editId: Int){
        if(editId != -1 && editId != taskID){
            taskID = editId

            viewModelScope.launch {
                val task: Task = repository.getTaskById(taskID).toTask()
                _tasksContentsTextState.value = task.contents
                contentsUndoStack = UndoRedoStack<String>().pushDefault(task.contents)
                dueToDateTimeState.value = task.dueTo
            }

        }
    }

    //undo/redo
    private var contentsUndoStack = UndoRedoStack<String>().pushDefault("")

    private var _undoShowState = mutableStateOf(false)
    val undoShowState: State<Boolean> = _undoShowState

    private var _redoShowState = mutableStateOf(false)
    val redoShowState: State<Boolean> = _redoShowState

    /**
     * Метод оновлення станів кнопок Undo/Redo
     */
    private fun updateUndoRedoStates(){
        _undoShowState.value = contentsUndoStack.isUndoActive
        _redoShowState.value = contentsUndoStack.isRedoActive
    }

    /**
     * Метод натиску на кнопку Redo
     */
    fun undoClicked(){
        _tasksContentsTextState.value = contentsUndoStack.undo().toString()
        updateUndoRedoStates()
        saveTaskClick()
    }

    /**
     * Метод натиску на кнопку Redo
     */
    fun redoClicked(){
        _tasksContentsTextState.value = contentsUndoStack.redo().toString()
        updateUndoRedoStates()
        saveTaskClick()
    }

    //contents
    private val _tasksContentsTextState = mutableStateOf("")
    val tasksContentsTextState: State<String> = _tasksContentsTextState
    /**
     * Метод зміни тексту вмісту [tasksContentsTextState] завдання
     * @param newText новий текст вмісту
     */
    fun tasksContentsTextChanged(newText: String){
        _tasksContentsTextState.value = newText
        contentsUndoStack.pushValue(newText)
        updateUndoRedoStates()
        saveTaskClick()
    }

    //datetime
    var dueToDateTimeState = mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))

    /**
     * Метод вибору дати і часу
     * @param context контекст додатку, потрібен для локалізації назв місяців
     */
    fun selectDateTime(context: Context) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                dueToDateTimeState.value = LocalDateTime(year = year, month = Month(month+1), dayOfMonth = day, hour = hour, minute = minute)
                saveTaskClick()
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()

    }

    //save
    /**
     * Метод збереження завдання
     * @return чи збереження успішне
     */
    fun saveTaskClick(): Boolean{
        return if (tasksContentsTextState.value.isNotBlank()){
            viewModelScope.launch {
                repository.insertTask(
                    Task(
                        id = taskID,
                        contents = _tasksContentsTextState.value,
                        dueTo = dueToDateTimeState.value,
                        isChecked = false
                    )
                )
            }
            true
        } else false
    }
}