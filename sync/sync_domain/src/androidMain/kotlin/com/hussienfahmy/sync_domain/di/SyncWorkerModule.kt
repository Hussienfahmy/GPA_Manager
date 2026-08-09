package com.hussienfahmy.sync_domain.di

import com.hussienfahmy.sync_domain.scheduler.BackgroundSyncScheduler
import com.hussienfahmy.sync_domain.worker.SyncWorkerUpload
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

// Split from syncDomainModule (commonMain) since WorkManager/koin-androidx-workmanager's worker{}
// DSL are Android-only. iOS's BackgroundSyncScheduler equivalent is registered in its own
// syncSchedulerModule (iosMain).
val syncWorkerModule = module {
    worker {
        SyncWorkerUpload(
            appContext = get(),
            workerParams = get(),
            syncUpload = get(),
            crashReporter = get()
        )
    }

    single { BackgroundSyncScheduler(workManager = get()) }
}
