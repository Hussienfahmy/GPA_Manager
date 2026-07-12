package com.hussienfahmy.core.domain.report

// STUB: both methods return String? already (the Android actual also returns null on any
// loading failure), so returning null here is a legitimate, already-handled outcome - the report
// simply renders without branding - rather than a crash. Real implementation needs to load the
// app icon from the iOS app bundle (Bundle.mainBundle) and a bundled QR code image resource,
// converting each to a base64 PNG.
actual class ReportBrandingProvider {
    actual suspend fun loadAppIconBase64Png(): String? = null

    actual suspend fun loadQrCodeBase64Png(): String? = null
}

actual fun createReportBrandingProvider(): ReportBrandingProvider = ReportBrandingProvider()
