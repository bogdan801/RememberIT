package com.bogdan801.rememberit.presentation.windows.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Це клас [SettingsViewModel], являє собою ViewModel для вікна налаштувань
 * @constructor в конструктор передається репозиторій
 * @param repository репозиторій, клас через методи якого надається доступ до локальної бази даних
 * @property showDialogState стан чи показувати діалогове вікно з попередженням про видалення всіх записів
 */
@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val repository: Repository
): ViewModel() {
    val showDialogState = mutableStateOf(false)

    /**
     * Метод для відображення діалогового вікна
     */
    fun showDeleteAllDialog(){
        showDialogState.value = true
    }

    /**
     * Метод натиску кнопки підтвердження видалення всіх записів
     */
    fun confirmDeleteClicked(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}