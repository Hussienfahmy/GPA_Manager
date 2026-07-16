package com.hussienfahmy.core.domain.report

// iOS actual (WKWebView + UIPrintPageRenderer/UIGraphicsPDFRenderer, the direct analog of
// Android's WebView+PrintManager) is deferred to the iOS phase, since nothing consumes it yet.
expect class PdfReportPrinter {
    fun print(html: String, title: String)
}

expect fun createPdfReportPrinter(): PdfReportPrinter
