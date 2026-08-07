package com.hussienfahmy.myGpaManager.di

import androidx.work.WorkManager
import org.koin.dsl.module

// The MainViewModel/MoreViewModel/AppOnBoardingGPATrackingViewModel registrations live in
// :shared's own sharedKoinModule alongside the ViewModel classes themselves. WorkManager stays
// here since background-sync scheduling is Android-only until iOS gets a BGTaskScheduler
// equivalent.
val appKoinModule = module {
    single { WorkManager.getInstance(get()) }
}
