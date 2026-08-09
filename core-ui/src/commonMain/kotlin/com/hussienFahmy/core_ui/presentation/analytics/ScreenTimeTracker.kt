package com.hussienfahmy.core_ui.presentation.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TrackScreenTime(screenName: String) {
    val analyticsLogger = koinInject<AnalyticsLogger>()
    val startTime = remember { Clock.System.now().toEpochMilliseconds().milliseconds }

    DisposableEffect(Unit) {
        onDispose {
            val timeSpent = (Clock.System.now().toEpochMilliseconds().milliseconds - startTime)
            analyticsLogger.logScreenTime(screenName, timeSpent.inWholeSeconds)
        }
    }
}