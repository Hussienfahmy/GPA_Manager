package com.hussienfahmy.sync_domain.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.hussienfahmy.core.domain.sync.SyncUpload
import java.util.concurrent.TimeUnit

class SyncWorkerUpload(
    appContext: Context,
    workerParams: WorkerParameters,
    private val syncUpload: SyncUpload
) : CoroutineWorker(
    appContext,
    workerParams
) {
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
                .setInitialDelay(5, TimeUnit.MINUTES) // 5 minute delay
                .build()
    }
}