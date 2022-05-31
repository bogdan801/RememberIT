package com.bogdan801.rememberit.presentation.windows.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This is [SettingsViewModel] class, it is a ViewModel for the Settings window
 * @constructor Repository is being passed to the constructor
 * @param repository class, methods of which grant access to the local database
 * @property showDialogState state which determines should the color theme list be shown
 */
@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val repository: Repository
): ViewModel() {
    val showDialogState = mutableStateOf(false)
    var visible = mutableStateOf(false)

    /**
     * Show dialog window method
     */
    fun showDeleteAllDialog(){
        showDialogState.value = true
    }

    /**
     * Confirm clearing of all entries method
     */
    fun confirmDeleteClicked(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}