package com.hussienfahmy.core_ui.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo

/**
 * [LocalWindowInfo.containerSize] is backed by Compose state on every target (Android, iOS, ...),
 * so - unlike reading UIScreen.mainScreen.bounds directly on iOS - this recomposes on rotation.
 */
@Composable
fun isLandscapeOrientation(): Boolean {
    val size = LocalWindowInfo.current.containerSize
    return size.width > size.height
}
