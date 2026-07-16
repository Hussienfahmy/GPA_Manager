package com.hussienfahmy.core.domain.report

// Loads the app icon / QR code as base64-encoded PNGs for embedding into the exported academic
// report HTML. iOS actual is deferred to the iOS phase, since nothing consumes it yet.
expect class ReportBrandingProvider {
    suspend fun loadAppIconBase64Png(): String?
    suspend fun loadQrCodeBase64Png(): String?
}

expect fun createReportBrandingProvider(): ReportBrandingProvider
