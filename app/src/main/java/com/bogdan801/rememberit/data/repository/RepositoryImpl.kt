package com.bogdan801.rememberit.data.repository

import com.bogdan801.rememberit.data.localdb.Dao
import com.bogdan801.rememberit.data.localdb.NoteEntity
import com.bogdan801.rememberit.data.localdb.TaskEntity
import com.bogdan801.rememberit.data.mapper.toFormatedString
import com.bogdan801.rememberit.data.mapper.toNoteEntity
import com.bogdan801.rememberit.data.mapper.toTaskEntity
import com.bogdan801.rememberit.domain.model.Note
import com.bogdan801.rememberit.domain.model.Task
import com.bogdan801.rememberit.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val dbDao: Dao
) : Repository {
    override fun getNotes(): Flow<List<NoteEntity>> = dbDao.getNotes()

    override fun getTasks(): Flow<List<TaskEntity>> = dbDao.getTasks()

    override suspend fun getNoteById(id: Int): NoteEntity = dbDao.getNoteByID(id)

    override suspend fun getTaskById(id: Int): TaskEntity = dbDao.getTaskByID(id)

    override suspend fun searchNotes(query: String): List<NoteEntity> = dbDao.searchNotes(query)

    override suspend fun searchTasks(query: String): List<TaskEntity> = dbDao.searchTasks(query)

    override suspend fun insertNote(note: Note) {
        dbDao.insertNoteEntity(note.toNoteEntity())
    }

    override suspend fun insertTask(task: Task) {
        dbDao.insertTaskEntity(task.toTaskEntity())
    }

    override suspend fun updateNote(note: Note) {
        dbDao.updateNote(
            noteID = note.id,
            title = note.title,
            contents = note.contents
        )
    }

    override suspend fun updateTask(task: Task) {
        dbDao.updateTaskContents(
            taskID = task.id,
            contents = task.contents,
            dueTo = task.dueTo.toFormatedString()
        )
    }

    override suspend fun updateTaskStatus(id: Int, isChecked: Boolean) {
        dbDao.updateTaskIsChecked(id, isChecked)
    }

    override suspend fun deleteNote(id: Int) {
        dbDao.deleteNote(id)
    }

    override suspend fun deleteTask(id: Int) {
        dbDao.deleteTask(id)
    }

    override suspend fun deleteAll() {
        dbDao.deleteNotes()
        dbDao.deleteTasks()
    }
}