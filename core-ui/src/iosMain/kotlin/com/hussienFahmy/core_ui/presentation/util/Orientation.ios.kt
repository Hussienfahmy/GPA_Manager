package com.hussienfahmy.core_ui.presentation.util

import androidx.compose.runtime.Composable
import platform.UIKit.UIScreen

// FLAG FOR REVIEW: reads UIScreen.mainScreen.bounds directly rather than through a reactive
// Compose signal, unlike the Android actual (LocalConfiguration.current is itself reactive).
// There's a known Compose Multiplatform iOS issue where composables don't always recompose on
// rotation (JetBrains/compose-multiplatform#3215) - this may need revisiting once the iOS app
// shell exists and rotation behavior can actually be observed on a device/simulator.
@Composable
actual fun isLandscapeOrientation(): Boolean {
    val bounds = UIScreen.mainScreen.bounds
    return bounds.useContents { size.width > size.height }
}
