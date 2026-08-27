package com.hussienfahmy.core.util

import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.generated.resources.store_name_app_store
import org.jetbrains.compose.resources.StringResource

// No App Store listing yet - see TODO.md's iOS parity gaps.
actual fun storeRatingUrl(): String? = null

actual val storeDisplayNameRes: StringResource = Res.string.store_name_app_store
