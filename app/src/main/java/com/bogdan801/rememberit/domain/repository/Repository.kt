package com.bogdan801.rememberit.domain.repository

import com.bogdan801.rememberit.data.localdb.NoteEntity
import com.bogdan801.rememberit.data.localdb.TaskEntity
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Інтерфейс репозиторію
 */
interface Repository {
    fun getNotes(): Flow<List<NoteEntity>>

    fun getTasks(): Flow<List<TaskEntity>>

    suspend fun getNoteById(id: Int): NoteEntity

    suspend fun getTaskById(id: Int): TaskEntity

    suspend fun searchNotes(query: String): List<NoteEntity>

    suspend fun searchTasks(query: String): List<TaskEntity>

    suspend fun insertNote(note: Note)

    suspend fun insertTask(task: Task)

    suspend fun updateNote(note: Note)

    suspend fun updateTask(task: Task)

    suspend fun updateTaskStatus(id: Int, isChecked: Boolean)

    suspend fun deleteNote(id: Int)

    suspend fun deleteTask(id: Int)

    suspend fun deleteAll()

    fun getMaxNoteId():Int?

    fun getMaxTaskId():Int?
}