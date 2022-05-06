package com.bogdan801.rememberit.domain.model

import kotlinx.datetime.LocalDateTime

data class Note(
    val id: Int = -1,
    val title: String,
    val contents: String,
    val dateTime: LocalDateTime
)

