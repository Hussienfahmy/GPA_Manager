package com.hussienfahmy.core.domain.report

// STUB: real implementation needs WKWebView (to render the report HTML) +
// UIPrintPageRenderer/UIGraphicsPDFRenderer (to rasterize it to a PDF and hand off to
// UIPrintInteractionController), the iOS analog of the Android actual's WebView+PrintManager.
// Not attempted blind - this involves nontrivial native UI/rendering-lifecycle code that needs a
// real device/simulator to get right, not just a plausible-looking Kotlin/Native interop call.
actual class PdfReportPrinter {
    actual fun print(html: String, title: String) {
        // TODO: WKWebView + UIPrintInteractionController implementation.
    }
}

actual fun createPdfReportPrinter(): PdfReportPrinter = PdfReportPrinter()
