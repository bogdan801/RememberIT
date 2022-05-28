package com.bogdan801.rememberit.domain.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.media.AudioAttributes
import android.media.RingtoneManager


const val channelID = "channel1"

fun Context.createNotificationChannel() {
    val name = "RememberIT notification channel"
    val desc = "Notification channel. Якщо ти це читаєш, гарного дня)"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(channelID, name, importance)

    val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_ALARM)
        .build()
    channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes)

    channel.description = desc
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}