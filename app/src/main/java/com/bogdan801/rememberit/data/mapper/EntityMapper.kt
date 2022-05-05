package com.bogdan801.rememberit.data.mapper

import com.bogdan801.rememberit.data.localdb.NoteEntity
import com.bogdan801.rememberit.data.localdb.TaskEntity
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.model.Task

fun NoteEntity.toNote(): Note = Note(id = id ?: 0, title = title, contents = contents, dateTime = dateTime.toLocalDateTime())
fun Note.toNoteEntity(): NoteEntity = NoteEntity(title = title, contents = contents, dateTime = dateTime.toFormatedString())

fun TaskEntity.toTask(): Task = Task(id = id ?: 0, contents = contents, dueTo = dueTo.toLocalDateTime(), isChecked = isChecked)
fun Task.toTaskEntity(): TaskEntity = TaskEntity(contents = contents, dueTo = dueTo.toFormatedString(), isChecked = isChecked)