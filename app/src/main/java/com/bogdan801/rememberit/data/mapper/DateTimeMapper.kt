package com.bogdan801.rememberit.data.mapper

import android.content.Context
import com.bogdan801.rememberit.R
import kotlinx.datetime.*
import java.time.Month

/**
 * Функція для конвертування LocalDateTime в String
 */
fun LocalDateTime.toFormattedString(): String = this.toInstant(TimeZone.currentSystemDefault()).toString()

/**
 * Функція для конвертування LocalDateTime в строку для відображення в UI
 */
fun LocalDateTime.toHumanReadableString(context: Context): String {
    val monthName = when(this.month){
        Month.JANUARY -> context.getText(R.string.jan)
        Month.FEBRUARY -> context.getText(R.string.feb)
        Month.MARCH -> context.getText(R.string.mar)
        Month.APRIL -> context.getText(R.string.apr)
        Month.MAY -> context.getText(R.string.may)
        Month.JUNE -> context.getText(R.string.jun)
        Month.JULY -> context.getText(R.string.jul)
        Month.AUGUST -> context.getText(R.string.aug)
        Month.SEPTEMBER -> context.getText(R.string.sep)
        Month.OCTOBER -> context.getText(R.string.oct)
        Month.NOVEMBER -> context.getText(R.string.nov)
        Month.DECEMBER -> context.getText(R.string.dec)
    }

    return "${this.dayOfMonth} $monthName ${this.year} ${"%02d".format(this.hour)}:${"%02d".format(this.minute)}"
}

/**
 * Функція для конвертування String в LocalDateTime
 */
fun String.toLocalDateTime(): LocalDateTime = Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())