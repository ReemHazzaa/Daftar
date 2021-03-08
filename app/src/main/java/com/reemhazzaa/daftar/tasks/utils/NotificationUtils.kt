package com.reemhazzaa.daftar.tasks.utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.reemhazzaa.daftar.R

class NotificationUtils(context: Context?): ContextWrapper(context) {
    private var mManager: NotificationManager? = null
    private val vibrationPattern = longArrayOf(2000, 2000, 2000, 2000, 2000)
    private val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

    companion object{
        const val channelID = "Daftar ChannelID"
        const val channelName = "Daftar Notification Channel"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        getManager()?.createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager? {
        if (mManager == null) {
            mManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        }
        return mManager
    }

    fun getChannelNotification(title: String?, body: String?): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notes)
            .setVibrate(vibrationPattern)
            .setSound(alarmSound)
    }
}