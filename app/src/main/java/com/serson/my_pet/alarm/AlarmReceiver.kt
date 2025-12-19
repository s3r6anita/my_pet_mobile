package com.serson.my_pet.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.serson.my_pet.MainActivity
import com.serson.my_pet.R
import com.serson.my_pet.navigation.Routes
import java.time.LocalDateTime
import java.time.ZoneId

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("notification_title") ?: "Процедура"
        val message = intent.getStringExtra("notification_message") ?: "Время выполнить процедуру"
        val notificationId =
            intent.getIntExtra("notification_id", 0) // Получим notificationId из intent

        // Создаем намерение для запуска MainActivity с необходимым маршрутом
        val resultIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("destination_route", "${Routes.Procedure.route}/$notificationId")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "notification_channel1")
            .setSmallIcon(R.drawable.pet_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // Устанавливаем PendingIntent для уведомления
            .setAutoCancel(true) // Автоматическое закрытие уведомления после нажатия
            .build()

        notificationManager.notify(
            notificationId,
            notification
        ) // Используем notificationId для уведомления
    }
}

fun scheduleNotification(
    context: Context,
    title: String,
    message: String,
    notificationTime: LocalDateTime,
    notificationId: Int
) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("notification_title", title)
        putExtra("notification_message", message)
        putExtra("notification_id", notificationId) // Добавим notificationId в intent
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        notificationId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val systemZoneId = ZoneId.systemDefault()
    val systemZonedDateTime = notificationTime.atZone(systemZoneId)
    val triggerAtMillis = systemZonedDateTime.toInstant().toEpochMilli()

    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
}
