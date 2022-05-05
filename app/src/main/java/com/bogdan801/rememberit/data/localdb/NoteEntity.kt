package com.bogdan801.rememberit.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity
data class NoteEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val contents: String,
    val dateTime: String
)