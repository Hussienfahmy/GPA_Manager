package com.hussienfahmy.core.util

import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.generated.resources.store_name_app_store
import org.jetbrains.compose.resources.StringResource

// App Store ID 6806832048 (App Store Connect). The apps.apple.com page only resolves once the
// app is live, but the "write-review" deep link is stable to ship now.
actual fun storeRatingUrl(): String? =
    "https://apps.apple.com/app/id6806832048?action=write-review"

actual val storeDisplayNameRes: StringResource = Res.string.store_name_app_store
