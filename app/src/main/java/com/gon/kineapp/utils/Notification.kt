package com.gon.kineapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.media.RingtoneManager
import android.os.Build
import com.gon.kineapp.R
import com.gon.kineapp.model.Exercise
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import java.util.*
import android.support.v4.app.TaskStackBuilder
import com.gon.kineapp.receivers.AlarmReceiver
import com.gon.kineapp.ui.activities.SplashActivity

object Notification {

    private val CHANNEL_ID: String = "CHANNEL_DEFAULT"
    private val ALARM_REQUEST_CODE = 600

    fun sendNotification(context: Context, title: String, message: String) {

        val notificationId = 1

        val pattern = longArrayOf(500, 500, 500, 500)

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationIntent = Intent(context, SplashActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)

        stackBuilder.addParentStack(SplashActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentTitle(title)
        builder.setContentText(message)
        builder.setSmallIcon(R.drawable.ic_excercises)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
        builder.setSound(uri)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setVibrate(pattern)
        builder.setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, builder.build())
    }

    fun setAlarm(context: Context, time: Long, title: String, message: String) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        lateinit var alarmIntent: PendingIntent
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra("TITLE", title)
            intent.putExtra("MESSAGE", message)
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        /*val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 1)
            val time = calendar.timeInMillis
        }*/

        alarmMgr.setExact(
            AlarmManager.RTC_WAKEUP,
            time,
            alarmIntent
        )
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply { description = descriptionText }

            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}