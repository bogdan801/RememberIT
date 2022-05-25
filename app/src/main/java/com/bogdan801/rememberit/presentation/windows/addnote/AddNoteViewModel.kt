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
 * Це клас [AddNoteViewModel], являє собою ViewModel для вікна додавання/редагування нотатки
 * @constructor в конструктор передається репозиторій
 * @param repository репозиторій, клас через методи якого надається доступ до локальної бази даних
 * @property noteID індекс нотатки для редагування, якщо він рівний -1, то буде створена нова нотатка, якщо ж ні,
 * то при збереженні буде відредагована нотатка під цим індексом
 * @property contentsUndoStack стак збереження і управління змінами вмісту нотатки(undo/redo функціонал)
 * @property undoShowState стан активності кнопки Undo
 * @property redoShowState стан активності кнопки Redo
 * @property notesTitleTextState стан тексту заголовкка нотатки
 * @property notesContentsTextState стан тексту вмісту нотатки
 * @property currentDateTime дата і час нотатки
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
     * Метод ініціалізації ViewModel для редагування нотатки
     * @param editId id нотатки для редагування
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
     * Метод оновлення станів кнопок Undo/Redo
     */
    private fun updateUndoRedoStates(){
        _undoShowState.value = contentsUndoStack.isUndoActive
        _redoShowState.value = contentsUndoStack.isRedoActive
    }

    /**
     * Метод натиску на кнопку Undo
     */
    fun undoClicked(){
        _notesContentsTextState.value = contentsUndoStack.undo().toString()
        updateUndoRedoStates()
        saveNoteClick()
    }

    /**
     * Метод натиску на кнопку Redo
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
     * Метод зміни тексту заголовку [notesTitleTextState] нотатки
     * @param newText новий текст заголовку
     */
    fun notesTitleTextChanged(newText: String){
        _notesTitleTextState.value = newText
        saveNoteClick()
    }

    //contents
    private val _notesContentsTextState = mutableStateOf("")
    val notesContentsTextState: State<String> = _notesContentsTextState
    /**
     * Метод зміни тексту вмісту [notesContentsTextState] нотатки
     * @param newText новий текст вмісту
     */
    fun notesContentsTextChanged(newText: String){
        _notesContentsTextState.value = newText
        contentsUndoStack.pushValue(newText)
        updateUndoRedoStates()
        saveNoteClick()
    }

    //datetime
    var currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    //save
    /**
     * Метод збереження нотатки
     * @return чи збереження успішне
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