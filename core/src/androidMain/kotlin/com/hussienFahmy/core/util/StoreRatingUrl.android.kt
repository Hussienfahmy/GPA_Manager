package com.hussienfahmy.core.util

import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.generated.resources.store_name_google_play
import org.jetbrains.compose.resources.StringResource

actual fun storeRatingUrl(): String? =
    "https://play.google.com/store/apps/details?id=com.hussienFahmy.myGpaManager"

actual val storeDisplayNameRes: StringResource = Res.string.store_name_google_play
