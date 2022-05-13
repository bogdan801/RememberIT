package com.bogdan801.rememberit.presentation.windows.settings

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val repository: Repository
): ViewModel() {
    val showDialogState = mutableStateOf(false)

    fun showDeleteAllDialog(){
        showDialogState.value = true
    }

    fun confirmDeleteClicked(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}