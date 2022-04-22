package com.bogdan801.rememberit.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey val id: Int? = null,
    val contents: String,
    val dueTo: String,
    val isChecked: Boolean
)
