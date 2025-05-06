package com.github.gouravkhunger.quotesapp

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.*
import androidx.work.ListenableWorker.Result
import com.github.gouravkhunger.quotesapp.workers.DailyQuoteWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class QuotesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                this.getString(R.string.daily_notif_id),
                this.getString(R.string.daily_notif_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = this.getString(R.string.daily_notif_desc)

            notificationManager.createNotificationChannel(channel)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequest
            .Builder(DailyQuoteWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                getString(R.string.daily_notif_id),
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

}
