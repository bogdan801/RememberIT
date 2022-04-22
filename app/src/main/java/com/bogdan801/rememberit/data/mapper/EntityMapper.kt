package com.bogdan801.rememberit.data.mapper

import com.bogdan801.rememberit.data.localdb.NoteEntity
import com.bogdan801.rememberit.data.localdb.TaskEntity
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.model.Task

fun NoteEntity.toNote(): Note = Note(title = title, contents = contents)
fun Note.toNoteEntity(): NoteEntity = NoteEntity(title = title, contents = contents)

fun TaskEntity.toTask(): Task = Task(contents = contents, dueTo = dueTo, isChecked = isChecked)
fun Task.toTaskEntity(): TaskEntity = TaskEntity(contents = contents, dueTo = dueTo, isChecked = isChecked)