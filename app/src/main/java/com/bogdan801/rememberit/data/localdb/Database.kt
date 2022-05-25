package com.bogdan801.rememberit.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Клас бези даних
 */
@Database(
    entities = [NoteEntity::class, TaskEntity::class],
    version = 2
)
abstract class Database : RoomDatabase() {
    abstract val dbDao: Dao
}