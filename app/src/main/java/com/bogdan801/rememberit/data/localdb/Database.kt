package com.bogdan801.rememberit.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * This is a database class
 */
@Database(
    entities = [NoteEntity::class, TaskEntity::class],
    version = 2
)
abstract class Database : RoomDatabase() {
    abstract val dbDao: Dao
}