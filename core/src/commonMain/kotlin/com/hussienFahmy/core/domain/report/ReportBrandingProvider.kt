package com.hussienfahmy.core.domain.report

import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.util.PlatformContext
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// Loads the app icon / QR code as base64-encoded PNGs for embedding into the exported academic
// report HTML.
expect class ReportBrandingProvider(context: PlatformContext, crashReporter: CrashReporter) {
    suspend fun loadAppIconBase64Png(): String?
    suspend fun loadQrCodeBase64Png(): String?
}

// Shared by both platform actuals - the QR code is a bundled Compose Multiplatform resource
// (core/src/commonMain/composeResources/drawable/qr_code.png), so loading + base64-encoding it is
// identical on every platform. The app *icon* stays platform-specific in each actual class,
// since "the current app's launcher icon" is a genuinely platform-specific query, not a bundled
// asset the app would otherwise carry a duplicate copy of.
@OptIn(ExperimentalEncodingApi::class)
internal suspend fun loadQrCodeBase64PngFromBundledResource(crashReporter: CrashReporter): String? =
    try {
        Base64.encode(Res.readBytes("drawable/qr_code.png"))
    } catch (e: Exception) {
        crashReporter.recordException(e, mapOf("operation" to "loadQrCodeBase64Png"))
        null
    }
