package com.bogdan801.rememberit.data.mapper

import com.bogdan801.rememberit.data.localdb.NoteEntity
import com.bogdan801.rememberit.data.localdb.TaskEntity
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.model.Task

/**
 * Function to convert NoteEntity into Note and vice versa
 */
fun NoteEntity.toNote(): Note = Note(id = id ?: 0, title = title, contents = contents, dateTime = dateTime.toLocalDateTime())
fun Note.toNoteEntity(): NoteEntity = NoteEntity(id = id, title = title, contents = contents, dateTime = dateTime.toFormattedString())

/**
 * Function to convert TaskEntity into Task and vice versa
 */
fun TaskEntity.toTask(): Task = Task(id = id ?: 0, contents = contents, dueTo = dueTo.toLocalDateTime(), isChecked = isChecked)
fun Task.toTaskEntity(): TaskEntity = TaskEntity(id = id, contents = contents, dueTo = dueTo.toFormattedString(), isChecked = isChecked)