package com.bogdan801.rememberit.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This is a task entity
 */
@Entity
data class TaskEntity(
    @PrimaryKey val id: Int,
    val contents: String,
    val dueTo: String,
    val isChecked: Boolean
)
