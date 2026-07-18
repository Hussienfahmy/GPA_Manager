package com.hussienfahmy.sync_domain.di

import com.hussienfahmy.sync_domain.worker.SyncWorkerUpload
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

// Split from syncDomainModule (commonMain) since WorkManager/koin-androidx-workmanager's worker{}
// DSL are Android-only. iOS's background-sync scheduling equivalent (BGTaskScheduler) is deferred
// to the iOS phase.
val syncWorkerModule = module {
    worker {
        SyncWorkerUpload(
            appContext = get(),
            workerParams = get(),
            syncUpload = get()
        )
    }
}
