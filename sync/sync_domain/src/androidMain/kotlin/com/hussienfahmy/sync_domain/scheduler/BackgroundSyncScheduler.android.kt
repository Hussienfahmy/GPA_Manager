package com.hussienfahmy.sync_domain.scheduler

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.hussienfahmy.sync_domain.worker.SyncWorkerUpload

actual class BackgroundSyncScheduler(
    private val workManager: WorkManager,
) {
    actual fun scheduleUploadSync() {
        workManager.enqueueUniqueWork(
            "upload_worker",
            ExistingWorkPolicy.REPLACE,
            SyncWorkerUpload.request,
        )
    }
}
