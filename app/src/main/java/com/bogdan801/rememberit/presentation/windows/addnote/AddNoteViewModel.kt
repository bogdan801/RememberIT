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

/**
 * This is [AddNoteViewModel] class, it is a ViewModel for the Add/Edit note window
 * @constructor Repository is being passed to the constructor
 * @param repository class, methods of which grant access to the local database
 * @property noteID id of current note(ether old for editing, or new to create note)
 * @property contentsUndoStack stack to save and manage changes to note content(undo/redo feature)
 * @property undoShowState Undo button activity status state
 * @property redoShowState Redo button activity status state
 * @property notesTitleTextState note title text status state
 * @property notesContentsTextState state of the text of the note content
 * @property currentDateTime date and time of the note
 */
@HiltViewModel
class AddNoteViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    //if edit initialisation
    private var noteID: Int = repository.getMaxNoteId()?.plus(1) ?: 1

    /**
     * ViewModel initialization method for note editing
     * @param editId note id for editing
     */
    fun initEditId(editId: Int){
        if(editId != -1 && editId != noteID){
            noteID = editId

            viewModelScope.launch {
                val note: Note = repository.getNoteById(noteID).toNote()
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

    /**
     * Method for updating Undo/Redo button states
     */
    private fun updateUndoRedoStates(){
        _undoShowState.value = contentsUndoStack.isUndoActive
        _redoShowState.value = contentsUndoStack.isRedoActive
    }

    /**
     * Undo click method
     */
    fun undoClicked(){
        _notesContentsTextState.value = contentsUndoStack.undo().toString()
        updateUndoRedoStates()
        saveNoteClick()
    }

    /**
     * Redo click method
     */
    fun redoClicked(){
        _notesContentsTextState.value = contentsUndoStack.redo().toString()
        updateUndoRedoStates()
        saveNoteClick()
    }

    //title
    private var _notesTitleTextState = mutableStateOf("")
    val notesTitleTextState: State<String> = _notesTitleTextState
    /**
     * Method of changing the title text [notesTitleTextState] of the note
     * @param newText new title text
     */
    fun notesTitleTextChanged(newText: String){
        _notesTitleTextState.value = newText
        saveNoteClick()
    }

    //contents
    private val _notesContentsTextState = mutableStateOf("")
    val notesContentsTextState: State<String> = _notesContentsTextState
    /**
     * Method of changing the content text [notesContentsTextState] of the note
     * @param newText new content text
     */
    fun notesContentsTextChanged(newText: String){
        _notesContentsTextState.value = newText
        contentsUndoStack.pushValue(newText)
        updateUndoRedoStates()
        saveNoteClick()
    }

    //datetime
    var currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    /**
     * Save note method
     * @return has it been saved successfully
     */
    fun saveNoteClick(): Boolean{
        return if (notesTitleTextState.value.isNotBlank() || notesContentsTextState.value.isNotBlank()){
            viewModelScope.launch {
                repository.insertNote(
                    Note(
                        id = noteID,
                        title = _notesTitleTextState.value,
                        contents = _notesContentsTextState.value,
                        dateTime = currentDateTime
                    )
                )
            }
            true
        } else false
    }
}