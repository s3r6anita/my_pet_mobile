package com.serson.my_pet

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Регистрация колбека для запроса прав на уведомления
    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // После решения пользователя по уведомлениям, проверяем будильники
        checkAndRequestExactAlarmPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createNotificationChannel(this)

        // Цепочка проверок: сначала Уведомления -> потом Будильники
        checkAndRequestNotificationPermission()

        val notificationRoute = intent.getStringExtra("destination_route")

        setContent {
            MyPetApp(notificationRoute)
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Запрашиваем права на уведомления
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Если права уже есть, переходим к проверке будильников
                checkAndRequestExactAlarmPermission()
            }
        } else {
            // Для старых Android права на уведомления не нужны, проверяем будильники
            checkAndRequestExactAlarmPermission()
        }
    }

    private fun checkAndRequestExactAlarmPermission() {
        // Проверка нужна только для Android 12 (S) и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(this, AlarmManager::class.java)

            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    // ВАЖНО: Указываем пакет, чтобы открыть настройки именно нашего приложения
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
            }
        }
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "notification_channel1"
            val channelName = "My Notification Channel1"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel description"
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
