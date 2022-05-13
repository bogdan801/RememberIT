package com.bogdan801.rememberit.presentation.windows.addnote

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.data.mapper.toNote
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.repository.Repository
import com.bogdan801.rememberit.presentation.windows.util.UndoRedoStack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private var editID = -1

    fun initEditId(editId: Int){
        if(editId != -1 && editId != editID){
            editID = editId

            viewModelScope.launch {
                val note: Note = repository.getNoteById(editID).toNote()
                _notesTitleTextState.value = note.title
                _notesContentsTextState.value = note.contents
                contentsUndoStack = UndoRedoStack<String>().pushDefault(note.contents)
                currentDateTime = note.dateTime
            }

        }
    }

    //undo/redo
    private var contentsUndoStack = UndoRedoStack<String>().pushDefault("")

    private var _undoShowState = mutableStateOf(false)
    val undoShowState: State<Boolean> = _undoShowState

    private var _redoShowState = mutableStateOf(false)
    val redoShowState: State<Boolean> = _redoShowState



    fun undoClicked(){
        _notesContentsTextState.value = contentsUndoStack.undo().toString()
        _undoShowState.value = contentsUndoStack.isUndoActive
        _redoShowState.value = contentsUndoStack.isRedoActive
    }

    fun redoClicked(){
        _notesContentsTextState.value = contentsUndoStack.redo().toString()
        _undoShowState.value = contentsUndoStack.isUndoActive
        _redoShowState.value = contentsUndoStack.isRedoActive
    }

    //title
    private var _notesTitleTextState = mutableStateOf("")
    val notesTitleTextState: State<String> = _notesTitleTextState
    fun notesTitleTextChanged(newText: String){
        _notesTitleTextState.value = newText
    }

    //contents
    private val _notesContentsTextState = mutableStateOf("")
    val notesContentsTextState: State<String> = _notesContentsTextState
    fun notesContentsTextChanged(newText: String){
        _notesContentsTextState.value = newText
        contentsUndoStack.pushValue(newText)
        _undoShowState.value = contentsUndoStack.isUndoActive
        _redoShowState.value = contentsUndoStack.isRedoActive
    }

    //datetime
    var currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    //save
    fun saveNoteClick(): Boolean{
        return if (notesTitleTextState.value.isNotBlank() || notesContentsTextState.value.isNotBlank()){
            viewModelScope.launch {
                if(editID == -1){
                    repository.insertNote(
                        Note(
                            title = _notesTitleTextState.value,
                            contents = _notesContentsTextState.value,
                            dateTime = currentDateTime
                        )
                    )
                }
                else{
                    repository.updateNote(
                        Note(
                            id = editID,
                            title = _notesTitleTextState.value,
                            contents = _notesContentsTextState.value,
                            dateTime = currentDateTime
                        )
                    )
                }
            }
            true
        } else false
    }
}