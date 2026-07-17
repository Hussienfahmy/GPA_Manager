package com.hussienfahmy.myGpaManager.di

import androidx.work.WorkManager
import org.koin.dsl.module

// The MainViewModel/MoreViewModel/AppOnBoardingGPATrackingViewModel registrations moved to
// :shared's own sharedKoinModule alongside the ViewModel classes themselves (Phase 11d). WorkManager
// stays here since background-sync scheduling is Android-only until the iOS phase implements its
// BGTaskScheduler equivalent.
val appKoinModule = module {
    single { WorkManager.getInstance(get()) }
}
