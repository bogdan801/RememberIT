package com.bogdan801.rememberit.presentation.windows.notes

import android.util.Log
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
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class NotesViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    //searchbar states
    private var _searchBarTextState = mutableStateOf("")
    private var _searchPlaceholderState = mutableStateOf("Search notes")
    var searchBarTextState: State<String> = _searchBarTextState
    var searchPlaceholderState: State<String> = _searchPlaceholderState

    fun searchBarValueChanged(newText: String, pageState: Int){
        _searchBarTextState.value = newText
        if(pageState == 0){
            Log.d("puk", "new text: $newText, page state: $pageState, note si")
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
        _searchPlaceholderState.value = if (pageState == 0) "Search notes" else "Search tasks"
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
    }

    //tasks states
    private val _allTasksState = mutableStateOf(listOf<Task>())
    private val _foundTasksState = mutableStateOf(listOf<Task>())
    var allTasksState: State<List<Task>> = _allTasksState
    var foundTasksState: State<List<Task>> = _foundTasksState

    fun taskDeleteClick(id :Int){
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun taskCheckedChanged(id :Int, status: Boolean){
        viewModelScope.launch {
            repository.updateTaskStatus(id, status)
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
            repository.getTasks().collect{ dbList ->
                _allTasksState.value = dbList.map { taskEntity->
                    taskEntity.toTask()
                }
            }
        }
    }
}
