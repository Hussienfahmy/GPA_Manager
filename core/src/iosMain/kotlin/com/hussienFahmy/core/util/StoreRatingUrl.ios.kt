package com.hussienfahmy.core.util

import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.generated.resources.store_name_app_store
import org.jetbrains.compose.resources.StringResource

actual fun storeRatingUrl(): String? =
    "https://apps.apple.com/app/id6806832048?action=write-review"

actual val storeDisplayNameRes: StringResource = Res.string.store_name_app_store
