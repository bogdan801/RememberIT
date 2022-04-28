package com.bogdan801.rememberit.domain.model

data class Task(
    val id: Int?,
    val contents: String,
    val dueTo: String,
    val isChecked: Boolean
)
