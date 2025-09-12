package com.hussienfahmy.sync_domain.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.hussienfahmy.core.R
import com.hussienfahmy.sync_domain.use_case.SyncUpload
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration

class SyncWorkerUpload(
    appContext: Context,
    workerParams: WorkerParameters,
    private val syncUpload: SyncUpload
) : CoroutineWorker(
    appContext,
    workerParams
) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        val id = "sync_channel"
        val notificationId = 1

        createChannel()

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle("Sync Data")
            .setContentText("Uploading your subjects and settings...")
            .setSmallIcon(R.drawable.baseline_sync_24)
            .setOngoing(true)
            .build()

        return ForegroundInfo(notificationId, notification)
    }

    private fun createChannel() {
        val id = "sync_channel"
        val name = "Sync Channel"
        val channel = NotificationChannel(
            id,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override suspend fun doWork(): Result {
        return try {
            syncUpload()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    companion object {
        val request
            get() = PeriodicWorkRequestBuilder<SyncWorkerUpload>(1.days.toJavaDuration())
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(false)
                        .setRequiresCharging(false)
                        .setRequiresDeviceIdle(false)
                        .build()
                )
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    1,
                    TimeUnit.HOURS
                )
                .setInitialDelay(1.hours.toJavaDuration())
                .build()
    }
}