package com.bogdan801.rememberit.domain.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

fun Context.scheduleNotification(notificationID: Int, title: String, message: String, time: Long) {
    val intent = Intent(applicationContext, AlarmReceiver::class.java)
    intent.putExtra(titleExtra, title)
    intent.putExtra(messageExtra, message)
    intent.putExtra(notificationIdExtra, notificationID)

    val pendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        notificationID,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        time,
        pendingIntent
    )
}

fun Context.cancelNotification(notificationID: Int) {
    val intent = Intent(applicationContext, AlarmReceiver::class.java)
    intent.putExtra(notificationIdExtra, notificationID)

    val pendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        notificationID,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    alarmManager.cancel(pendingIntent)
}



