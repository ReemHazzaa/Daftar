package com.reemhazzaa.daftar.tasks.data.viewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.tasks.data.models.Task
import com.reemhazzaa.daftar.tasks.receivers.AlertReceiver
import java.util.*

class SharedViewModel (application: Application): AndroidViewModel(application) {
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(toDoData: List<Task>){
        emptyDatabase.value = toDoData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            when(position){
                0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red_dark)) }
                1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow_dark)) }
                2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green_dark)) }
            }
        }
    }

    fun startAlarm(c: Calendar, ctx: Context) {
        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(ctx,
            1, intent, 0)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        } else {
            Toast.makeText(ctx, ctx.getString(R.string.invalid_date), Toast.LENGTH_SHORT).show()
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    fun cancelAlarm(ctx: Context) {
        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(ctx,
            1, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}