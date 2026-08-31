package com.hussienfahmy.sync_domain.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.core.domain.sync.SyncUpload

class SyncWorkerUpload(
    appContext: Context,
    workerParams: WorkerParameters,
    private val syncUpload: SyncUpload,
    private val crashReporter: CrashReporter
) : CoroutineWorker(
    appContext,
    workerParams
) {
    override suspend fun doWork(): Result {
        return try {
            syncUpload()
            Result.success()
        } catch (e: Exception) {
            crashReporter.recordException(e, mapOf("worker" to "SyncWorkerUpload"))
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