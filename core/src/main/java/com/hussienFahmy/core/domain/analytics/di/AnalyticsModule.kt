package com.hussienfahmy.core.domain.analytics.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.hussienfahmy.core.data.analytics.FirebaseAnalyticsService
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val analyticsModule = module {
    single<FirebaseAnalytics> {
        Firebase.analytics
    }

    single<AnalyticsService> {
        FirebaseAnalyticsService(get())
    }

    single<AnalyticsLogger> {
        AnalyticsLogger(get())
    }
}