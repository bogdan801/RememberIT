package com.bogdan801.rememberit.data.mapper

import kotlinx.datetime.*
import java.time.Month

fun LocalDateTime.toFormattedString(): String = this.toInstant(TimeZone.currentSystemDefault()).toString()

fun LocalDateTime.toHumanReadableString(): String {
    val monthName = when(this.month){
        Month.JANUARY -> "January"
        Month.FEBRUARY -> "February"
        Month.MARCH -> "March"
        Month.APRIL -> "April"
        Month.MAY -> "May"
        Month.JUNE -> "Jun"
        Month.JULY -> "July"
        Month.AUGUST -> "August"
        Month.SEPTEMBER -> "September"
        Month.OCTOBER -> "October"
        Month.NOVEMBER -> "November"
        Month.DECEMBER -> "December"
    }

    return "${this.dayOfMonth} $monthName ${this.year} ${"%02d".format(this.hour)}:${"%02d".format(this.minute)}"
}

fun String.toLocalDateTime(): LocalDateTime = Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())