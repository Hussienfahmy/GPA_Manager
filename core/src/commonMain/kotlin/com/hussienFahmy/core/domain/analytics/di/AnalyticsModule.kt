package com.hussienfahmy.core.domain.analytics.di

import com.hussienfahmy.core.data.analytics.FirebaseAnalyticsService
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.FirebaseAnalytics
import dev.gitlive.firebase.analytics.analytics
import org.koin.dsl.module

val analyticsModule = module {
    single<FirebaseAnalytics> {
        Firebase.analytics
    }

    single<AnalyticsService> {
        FirebaseAnalyticsService(get())
    }

    single<AnalyticsLogger> {
        AnalyticsLogger(get(), get(), get(), get())
    }
}