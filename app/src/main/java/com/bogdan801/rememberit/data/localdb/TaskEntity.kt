package com.bogdan801.rememberit.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сутність завдання для бази даних
 */
@Entity
data class TaskEntity(
    @PrimaryKey val id: Int,
    val contents: String,
    val dueTo: String,
    val isChecked: Boolean
)
