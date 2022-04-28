package com.bogdan801.rememberit.data.mapper

import com.bogdan801.rememberit.data.localdb.NoteEntity
import com.bogdan801.rememberit.data.localdb.TaskEntity
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.model.Task

fun NoteEntity.toNote(): Note = Note(id = id, title = title, contents = contents, dateTime = dateTime)
fun Note.toNoteEntity(): NoteEntity = NoteEntity(title = title, contents = contents, dateTime = dateTime)

fun TaskEntity.toTask(): Task = Task(id = id, contents = contents, dueTo = dueTo, isChecked = isChecked)
fun Task.toTaskEntity(): TaskEntity = TaskEntity(contents = contents, dueTo = dueTo, isChecked = isChecked)