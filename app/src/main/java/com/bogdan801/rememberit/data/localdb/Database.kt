package com.bogdan801.rememberit.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NoteEntity::class, TaskEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract val dbDao: Dao
}