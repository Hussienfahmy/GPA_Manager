package com.hussienfahmy.core.domain.report

import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.util.PlatformContext
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// Loads the app icon / QR code as base64-encoded PNGs for embedding into the exported academic
// report HTML. Only the icon load is expect/actual (needs PackageManager/Bitmap on Android,
// UIImage/NSBundle on iOS) - the QR code is a bundled Compose Multiplatform resource
// (core/src/commonMain/composeResources/drawable/qr_code.png), so loading + base64-encoding it is
// identical on every platform and lives here directly instead of behind a second expect/actual pair.
@OptIn(ExperimentalEncodingApi::class)
class ReportBrandingProvider(
    private val context: PlatformContext,
    private val crashReporter: CrashReporter,
) {
    suspend fun loadAppIconBase64Png(): String? = loadAppIconBase64Png(context, crashReporter)

    suspend fun loadQrCodeBase64Png(): String? =
        try {
            Base64.encode(Res.readBytes("drawable/qr_code.png"))
        } catch (e: Exception) {
            crashReporter.recordException(e, mapOf("operation" to "loadQrCodeBase64Png"))
            null
        }
}

internal expect suspend fun loadAppIconBase64Png(
    context: PlatformContext,
    crashReporter: CrashReporter,
): String?
