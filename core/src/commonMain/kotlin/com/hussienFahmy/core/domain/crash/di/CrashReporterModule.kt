package com.hussienfahmy.core.domain.crash.di

import com.hussienFahmy.core.data.crash.FirebaseCrashReporter
import com.hussienfahmy.core.domain.crash.CrashReporter
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.FirebaseCrashlytics
import dev.gitlive.firebase.crashlytics.crashlytics
import org.koin.dsl.module

val crashReporterModule = module {
    single<FirebaseCrashlytics> {
        Firebase.crashlytics
    }

    single<CrashReporter> {
        FirebaseCrashReporter(get())
    }
}
