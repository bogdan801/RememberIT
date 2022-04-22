package com.bogdan801.rememberit.domain.model

data class Task(
    val contents: String,
    val dueTo: String,
    val isChecked: Boolean
)
