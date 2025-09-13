package com.hussienfahmy.sync_domain.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.sync.SyncUpload

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
            get() = OneTimeWorkRequestBuilder<SyncWorkerUpload>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(false)
                        .setRequiresCharging(false)
                        .setRequiresDeviceIdle(false)
                        .build()
                )
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
    }
}