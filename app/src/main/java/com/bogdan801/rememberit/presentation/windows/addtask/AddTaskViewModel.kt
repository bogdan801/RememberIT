package com.bogdan801.rememberit.presentation.windows.addtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.data.mapper.toTask
import com.bogdan801.rememberit.domain.model.Task
import com.bogdan801.rememberit.domain.repository.Repository
import com.bogdan801.rememberit.presentation.windows.util.UndoRedoStack
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*
import javax.inject.Inject

/**
 * This is [AddTaskViewModel] class, it is a ViewModel for the Add/Edit task window
 * @constructor Repository is being passed to the constructor
 * @param repository репозиторій, клас через методи якого надається доступ до локальної бази даних
 * @property taskID id of current task(ether old for editing, or new to create task)
 * @property contentsUndoStack stack to save and manage changes to note content(undo/redo feature)
 * @property undoShowState Undo button activity status state
 * @property redoShowState Redo button activity status state
 * @property tasksContentsTextState state of the text of the task content
 * @property dueToDateTimeState date and time of the task state
 */
@HiltViewModel
class AddTaskViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private var taskID: Int = repository.getMaxTaskId()?.plus(1) ?: 1

    /**
     * ViewModel initialization method for task editing
     * @param editId task id id for editing
     */
    fun initEditId(editId: Int){
        if(editId != -1 && editId != taskID){
            taskID = editId

            viewModelScope.launch {
                val task: Task = repository.getTaskById(taskID).toTask()
                _tasksContentsTextState.value = task.contents
                contentsUndoStack = UndoRedoStack<String>().pushDefault(task.contents)
                dueToDateTimeState.value = task.dueTo
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
        _tasksContentsTextState.value = contentsUndoStack.undo().toString()
        updateUndoRedoStates()
        saveTaskClick()
    }

    /**
     * Redo click method
     */
    fun redoClicked(){
        _tasksContentsTextState.value = contentsUndoStack.redo().toString()
        updateUndoRedoStates()
        saveTaskClick()
    }

    //contents
    private val _tasksContentsTextState = mutableStateOf("")
    val tasksContentsTextState: State<String> = _tasksContentsTextState
    /**
     * Method of changing the content text [tasksContentsTextState] of the task
     * @param newText new content text
     */
    fun tasksContentsTextChanged(newText: String){
        _tasksContentsTextState.value = newText
        contentsUndoStack.pushValue(newText)
        updateUndoRedoStates()
        saveTaskClick()
    }

    //datetime
    var dueToDateTimeState = mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))

    /**
     * Method to pick data and time for a task
     * @param context of an app
     */
    fun selectDateTime(context: Context) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                dueToDateTimeState.value = LocalDateTime(year = year, month = Month(month+1), dayOfMonth = day, hour = hour, minute = minute)
                saveTaskClick()
            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }

    //save
    /**
     * Save task method
     * @return has it been saved successfully
     */
    fun saveTaskClick(): Boolean{
        return if (tasksContentsTextState.value.isNotBlank()){
            viewModelScope.launch {
                repository.insertTask(
                    Task(
                        id = taskID,
                        contents = _tasksContentsTextState.value,
                        dueTo = dueToDateTimeState.value,
                        isChecked = false
                    )
                )
            }
            true
        } else false
    }
}