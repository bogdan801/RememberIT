package com.bogdan801.rememberit.presentation.windows.addnote


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.repository.Repository
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
    //title
    var notesTitleTextState = mutableStateOf("")
    fun notesTitleTextChanged(newText: String){
        notesTitleTextState.value = newText
    }

    //contents
    var notesContentsTextState = mutableStateOf("")
    fun notesContentsTextChanged(newText: String){
        notesContentsTextState.value = newText
    }

    //datetime
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    //save
    fun saveNoteClick(): Boolean{
        return if (notesTitleTextState.value.isNotBlank() || notesContentsTextState.value.isNotBlank()){
            viewModelScope.launch {
                repository.insertNote(
                    Note(
                        title = notesTitleTextState.value,
                        contents = notesContentsTextState.value,
                        dateTime = currentDateTime
                    )
                )
            }
            true
        } else false
    }
}