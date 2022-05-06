package com.bogdan801.rememberit.presentation.windows.notes

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val repository: Repository
) : ViewModel() {

    //searchbar states
    var searchBarTextState = mutableStateOf("")
    var searchPlaceholderState = mutableStateOf("Search notes")

    fun searchBarValueChanged(newText: String, pageState: Int){
        searchBarTextState.value = newText

        if(pageState == 0){
            if(searchBarTextState.value.isNotBlank()){
                viewModelScope.launch {
                    _foundNotesState.value = repository.searchNotes(newText).map { it.toNote() }
                    notesState = _foundNotesState
                }
            }
            else  notesState = _allNotesState
        }

        if(pageState == 1){
            if(searchBarTextState.value.isNotBlank()){
                viewModelScope.launch {
                    _foundTasksState.value = repository.searchTasks(newText).map { it.toTask() }
                    tasksState = _foundTasksState
                }
            }
            else  tasksState = _allTasksState
        }
    }

    fun setPlaceholder(pageState: Int){
        searchPlaceholderState.value = if (pageState == 0) "Search notes" else "Search tasks"
    }

    //notes states
    private val _allNotesState = mutableStateOf(listOf<Note>())
    private val _foundNotesState = mutableStateOf(listOf<Note>())
    var notesState: State<List<Note>> = _allNotesState

    //tasks states
    private val _allTasksState = mutableStateOf(listOf<Task>())
    private val _foundTasksState = mutableStateOf(listOf<Task>())
    var tasksState: State<List<Task>> = _allTasksState

    init {
        viewModelScope.launch {
            repository.getNotes().collect{ dbList ->
                _allNotesState.value = dbList.map { noteEntity->
                    noteEntity.toNote()
                }
            }
            repository.getTasks().collect{ dbList ->
                _allTasksState.value = dbList.map { taskEntity->
                    taskEntity.toTask()
                }
            }
        }
    }

    public fun noteDeleteClick(id :Int){
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    public fun taskDeleteClick(id :Int){
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    public fun taskCheckedChanged(id :Int, status: Boolean){
        viewModelScope.launch {
            repository.updateTaskStatus(id, status)
        }
    }
}