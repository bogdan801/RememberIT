package com.bogdan801.rememberit.data.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * This is Data Access Object interface
 */
@Dao
interface Dao {
    //insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteEntity(noteEntity: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskEntity(taskEntity: TaskEntity)

    //update
    @Query("UPDATE noteentity SET title = :title, contents = :contents WHERE id = :noteID")
    suspend fun updateNote(noteID: Int, title: String, contents: String)

    @Query("UPDATE taskentity SET isChecked = :isChecked WHERE id = :taskID")
    suspend fun updateTaskIsChecked(taskID: Int, isChecked: Boolean)

    @Query("UPDATE taskentity SET contents = :contents, dueTo = :dueTo WHERE id = :taskID")
    suspend fun updateTaskContents(taskID: Int, contents: String, dueTo: String)

    //delete
    @Query("DELETE FROM noteentity WHERE id == :noteID")
    suspend fun deleteNote(noteID: Int)

    @Query("DELETE FROM noteentity")
    suspend fun deleteNotes()

    @Query("DELETE FROM taskentity WHERE id == :taskID")
    suspend fun deleteTask(taskID: Int)

    @Query("DELETE FROM taskentity")
    suspend fun deleteTasks()

    //search
    @Query("""
        SELECT * 
        FROM noteentity 
        WHERE LOWER(title) LIKE '%' || LOWER(:searchQuery) || '%' OR LOWER(contents) LIKE '%' || LOWER(:searchQuery) || '%'
    """)
    suspend fun searchNotes(searchQuery: String) : List<NoteEntity>

    @Query("""
        SELECT * 
        FROM taskentity 
        WHERE LOWER(contents) LIKE '%' || LOWER(:searchQuery) || '%' OR LOWER(dueTo) LIKE '%' || LOWER(:searchQuery) || '%'
    """)
    suspend fun searchTasks(searchQuery: String) : List<TaskEntity>

    //select
    @Query("SELECT * FROM noteentity")
    fun getNotes() : Flow<List<NoteEntity>>

    @Query("SELECT * FROM taskentity")
    fun getTasks() : Flow<List<TaskEntity>>

    @Query("SELECT * FROM noteentity WHERE id = :id")
    suspend fun getNoteByID(id: Int) : NoteEntity

    @Query("SELECT * FROM taskentity WHERE id = :id")
    suspend fun getTaskByID(id: Int) : TaskEntity

    @Query("SELECT MAX(id) FROM noteentity")
    suspend fun getMaxNoteId(): Int?

    @Query("SELECT MAX(id) FROM taskentity")
    suspend fun getMaxTaskId(): Int?
}