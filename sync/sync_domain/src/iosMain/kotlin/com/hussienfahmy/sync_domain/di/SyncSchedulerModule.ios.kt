package com.hussienfahmy.sync_domain.di

import com.hussienfahmy.sync_domain.scheduler.BackgroundSyncScheduler
import org.koin.dsl.module

// Android's equivalent (BackgroundSyncScheduler wrapping WorkManager) lives in syncWorkerModule
// (androidMain) - split the same way since the two actuals take entirely different constructor
// dependencies (WorkManager vs. SyncUpload/CrashReporter).
val syncSchedulerModule = module {
    single { BackgroundSyncScheduler(syncUpload = get(), crashReporter = get()) }
}
