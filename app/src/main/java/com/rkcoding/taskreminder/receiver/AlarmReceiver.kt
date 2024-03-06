package com.rkcoding.taskreminder.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.rkcoding.taskreminder.R
import com.rkcoding.taskreminder.core.utils.Constants

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("TASK_REMINDER") ?: return

        val defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        context?.let { cxt ->
            val notificationManager = cxt.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(cxt, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notificaton_info)
                .setContentTitle("Task Reminder")
                .setContentText("Notification message: $message")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setSound(defaultUri)
                .setContentIntent(ServiceHelper.clickPendingIntent(cxt))
            notificationManager.notify(1, builder.build())
        }
    }
}