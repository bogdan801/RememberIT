package com.bogdan801.rememberit.presentation.windows.addtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.rememberit.data.mapper.toTask
import com.bogdan801.rememberit.domain.model.Task
import com.bogdan801.rememberit.domain.repository.Repository
import com.bogdan801.rememberit.presentation.windows.util.UndoRedoStack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private var editID = -1

    fun initEditId(editId: Int){
        if(editId != -1 && editId != editID){
            editID = editId

            viewModelScope.launch {
                val task: Task = repository.getTaskById(editID).toTask()
                _tasksContentsTextState.value = task.contents
                contentsUndoStack = UndoRedoStack<String>().pushDefault(task.contents)
                dueToDateTime.value = task.dueTo
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
        _tasksContentsTextState.value = contentsUndoStack.undo().toString()
        _undoShowState.value = contentsUndoStack.isUndoActive
        _redoShowState.value = contentsUndoStack.isRedoActive
    }

    fun redoClicked(){
        _tasksContentsTextState.value = contentsUndoStack.redo().toString()
        _undoShowState.value = contentsUndoStack.isUndoActive
        _redoShowState.value = contentsUndoStack.isRedoActive
    }

    //contents
    private val _tasksContentsTextState = mutableStateOf("")
    val tasksContentsTextState: State<String> = _tasksContentsTextState
    fun tasksContentsTextChanged(newText: String){
        _tasksContentsTextState.value = newText
        contentsUndoStack.pushValue(newText)
        _undoShowState.value = contentsUndoStack.isUndoActive
        _redoShowState.value = contentsUndoStack.isRedoActive
    }

    //datetime
    var dueToDateTime = mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))

    fun selectDateTime(context: Context) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                dueToDateTime.value = LocalDateTime(year = year, month = Month(month+1), dayOfMonth = day, hour = hour, minute = minute)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    //save
    fun saveTaskClick(): Boolean{
        return if (tasksContentsTextState.value.isNotBlank()){
            viewModelScope.launch {
                if(editID == -1){
                    repository.insertTask(
                        Task(
                            contents = _tasksContentsTextState.value,
                            dueTo = dueToDateTime.value,
                            isChecked = false
                        )
                    )
                }
                else{
                    repository.updateTask(
                        Task(
                            id = editID,
                            contents = _tasksContentsTextState.value,
                            dueTo = dueToDateTime.value,
                            isChecked = false
                        )
                    )
                }
            }
            true
        } else false
    }
}