package com.hussienfahmy.sync_domain.scheduler

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker
import com.hussienfahmy.sync_domain.worker.SyncWorkerUpload

actual class BackgroundSyncScheduler(
    private val workManager: WorkManager,
    private val dirtyTracker: SyncDirtyTracker,
) {
    actual fun scheduleUploadSync() {
        if (!dirtyTracker.hasAnyChanges()) return

        workManager.enqueueUniqueWork(
            "upload_worker",
            ExistingWorkPolicy.REPLACE,
            SyncWorkerUpload.request,
        )
    }
}
