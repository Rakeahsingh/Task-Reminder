package com.rkcoding.taskreminder.todo_features.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.rkcoding.taskreminder.todo_features.domain.model.AlarmItem
import com.rkcoding.taskreminder.todo_features.domain.repository.AlarmScheduler
import java.time.ZoneId

class AlarmSchedulerImpl(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun schedule(alarmItem: AlarmItem) {
        val intent = Intent(context, AlarmManager::class.java)
        intent.putExtra("TASK_REMINDER", alarmItem.message)

        val alarmTime = alarmItem.alarmTime.atZone(ZoneId.systemDefault()).toEpochSecond()*1000L
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmItem.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
        Log.d("Alarm", "schedule: alarm $alarmTime ")
    }

    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, AlarmManager::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}