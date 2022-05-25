package com.bogdan801.rememberit.domain.model

import kotlinx.datetime.LocalDateTime

/**
 * Модель завдання
 */
data class Task(
    val id: Int = -1,
    val contents: String,
    val dueTo: LocalDateTime,
    val isChecked: Boolean
)
