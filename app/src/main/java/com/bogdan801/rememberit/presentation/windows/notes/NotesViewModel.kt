package com.bogdan801.rememberit.presentation.windows.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.data.localdb.NoteEntity
import com.bogdan801.rememberit.data.mapper.toNote
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.model.Task
import com.bogdan801.rememberit.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private val _notesState = mutableStateOf(listOf<Note>())
    val notesState: State<List<Note>> = _notesState

    private val _tasksState = mutableStateOf(listOf<Task>())
    val tasksState: State<List<Task>> = _tasksState

    init {
        viewModelScope.launch {
            repository.getNotes().collect{ dbList ->
                _notesState.value = dbList.map { noteEntity->
                    noteEntity.toNote()
                }
            }
        }


    }




}