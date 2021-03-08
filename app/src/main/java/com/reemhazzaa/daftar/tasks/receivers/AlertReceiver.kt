package com.reemhazzaa.daftar.tasks.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.tasks.utils.NotificationUtils
import java.util.*

class AlertReceiver : BroadcastReceiver() {
    lateinit var nb: NotificationCompat.Builder
    val id = Random(System.currentTimeMillis()).nextInt(1000)

    override fun onReceive(context: Context?, intent: Intent?) {
        val nu = NotificationUtils(context?.applicationContext)
        nb = nu.getChannelNotification(
            context?.getString(R.string.task_alert), context?.getString(R.string.task_reminder_body)
        )
        nu.getManager()?.notify(id, nb.build())
    }
}