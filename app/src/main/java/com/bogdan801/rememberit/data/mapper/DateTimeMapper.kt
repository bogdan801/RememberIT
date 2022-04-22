package com.bogdan801.rememberit.data.mapper

import kotlinx.datetime.*

fun LocalDateTime.toFormatedString(): String = this.toInstant(TimeZone.currentSystemDefault()).toString()
fun String.toLocalDateTime(): LocalDateTime = Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())