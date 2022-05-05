package com.bogdan801.rememberit.domain.model

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Int,
    val contents: String,
    val dueTo: LocalDateTime,
    val isChecked: Boolean
)
