package com.bogdan801.rememberit.domain.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

/**
 * Function to schedule a notification
 * @param notificationID id of a notification
 * @param title of the notification
 * @param message of the notification
 * @param time at which notification will be sent to device
 */
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

/**
 * Function to cancel scheduled notification by id
 * @param notificationID id of notification to cancel
 */
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



