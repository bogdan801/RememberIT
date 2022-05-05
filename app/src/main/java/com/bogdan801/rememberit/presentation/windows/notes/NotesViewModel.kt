package com.bogdan801.rememberit.presentation.windows.notes

import androidx.lifecycle.ViewModel
import com.bogdan801.rememberit.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotesViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

}