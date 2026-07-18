package com.hussienfahmy.core_ui.presentation.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TrackScreenTime(screenName: String) {
    val analyticsLogger = koinInject<AnalyticsLogger>()
    val startTime = remember { System.currentTimeMillis().milliseconds }

    DisposableEffect(Unit) {
        onDispose {
            val timeSpent = (System.currentTimeMillis().milliseconds - startTime)
            analyticsLogger.logScreenTime(screenName, timeSpent.inWholeSeconds)
        }
    }
}