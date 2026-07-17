package com.hussienfahmy.core_ui.presentation.util

import androidx.compose.runtime.Composable

/**
 * Compose Multiplatform has no shared orientation API (LocalConfiguration is Android-only) -
 * this is the minimal expect/actual needed to keep screens that branch on orientation
 * (portrait/landscape layouts) in commonMain.
 */
@Composable
expect fun isLandscapeOrientation(): Boolean
