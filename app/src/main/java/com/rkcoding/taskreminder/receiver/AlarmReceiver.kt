package com.rkcoding.taskreminder.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.core.app.NotificationCompat
import com.google.android.play.integrity.internal.c
import com.rkcoding.taskreminder.R
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.Priority

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("TASK_REMINDER") ?: return

        val channelId = "alarm_id"
        val defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        context?.let { cxt ->
            val notificationManager = cxt.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(cxt, channelId)
                .setSmallIcon(R.drawable.notificaton_info)
                .setContentTitle("Task Reminder")
                .setContentText("Notification send with message: $message")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setSound(defaultUri)
            notificationManager.notify(1, builder.build())
        }
    }
}