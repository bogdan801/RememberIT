package com.bogdan801.rememberit.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

/**
 * This is a note entity
 */
@Entity
data class NoteEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val contents: String,
    val dateTime: String
)