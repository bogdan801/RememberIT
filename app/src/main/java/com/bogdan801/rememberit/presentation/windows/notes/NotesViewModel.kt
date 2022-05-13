package com.bogdan801.rememberit.presentation.windows.notes

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.BaseApplication
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.data.mapper.toNote
import com.bogdan801.rememberit.data.mapper.toTask
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.model.Task
import com.bogdan801.rememberit.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    fun setPlaceholder(pageState: Int){
        _searchPlaceholderState.value = if (pageState == 0) application.getString(R.string.search_notes) else application.getString(R.string.search_tasks)
    }

    //notes states
    private val _allNotesState = mutableStateOf(listOf<Note>())
    private val _foundNotesState = mutableStateOf(listOf<Note>())
    var allNotesState: State<List<Note>> = _allNotesState
    var foundNotesState: State<List<Note>> = _foundNotesState

    fun noteDeleteClick(id :Int){
        viewModelScope.launch {
            repository.deleteNote(id)
        }

        if(_searchBarTextState.value.isNotBlank()){
            viewModelScope.launch {
                _foundNotesState.value = repository.searchNotes(_searchBarTextState.value).map { it.toNote() }
            }
        }
    }

    //tasks states
    private val _allTasksState = mutableStateOf(listOf<Task>())
    private val _foundTasksState = mutableStateOf(listOf<Task>())
    var allTasksState: State<List<Task>> = _allTasksState
    var foundTasksState: State<List<Task>> = _foundTasksState

    fun taskDeleteClick(id :Int){
        viewModelScope.launch {
            repository.deleteTask(id)
        }

        if(_searchBarTextState.value.isNotBlank()){
            viewModelScope.launch {
                _foundTasksState.value = repository.searchTasks(_searchBarTextState.value).map { it.toTask() }
            }
        }
    }

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

    //on viewmodel initialization
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
                _allTasksState.value = dbList.map { taskEntity->
                    taskEntity.toTask()
                }
            }
        }
    }
}
