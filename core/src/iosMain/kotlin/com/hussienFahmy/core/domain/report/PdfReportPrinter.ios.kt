package com.hussienfahmy.core.domain.report

import platform.Foundation.NSObject
import platform.UIKit.UIPrintInfo
import platform.UIKit.UIPrintInfoOutputType
import platform.UIKit.UIPrintInteractionController
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView

// The iOS analog of the Android actual's WebView+PrintManager: render the report HTML in an
// off-screen WKWebView, then hand its print formatter to UIPrintInteractionController, which
// shows the system print sheet - the same shape as Android's WebView.createPrintDocumentAdapter
// + PrintManager. Best-effort, unverified - no simulator/device available in this sandbox to
// confirm the WKNavigationDelegate callback signature or the print sheet's presentation.
actual class PdfReportPrinter {
    // Held strongly until the print flow completes: WKWebView.navigationDelegate is a weak
    // reference in UIKit, so this async webView/delegate pair would otherwise be eligible for
    // Kotlin/Native GC before didFinishNavigation ever fires.
    private var activeWebView: WKWebView? = null
    private var activeDelegate: NSObject? = null

    actual fun print(html: String, title: String) {
        val webView = WKWebView()
        val delegate = object : NSObject(), WKNavigationDelegateProtocol {
            override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
                presentPrintController(webView, title)
                activeWebView = null
                activeDelegate = null
            }
        }
        activeWebView = webView
        activeDelegate = delegate
        webView.navigationDelegate = delegate
        webView.loadHTMLString(html, baseURL = null)
    }

    private fun presentPrintController(webView: WKWebView, title: String) {
        val printInfo = UIPrintInfo.printInfo()
        printInfo.outputType = UIPrintInfoOutputType.UIPrintInfoOutputGeneral
        printInfo.jobName = title

        val printController = UIPrintInteractionController.sharedPrintController()
        printController?.printInfo = printInfo
        printController?.printFormatter = webView.viewPrintFormatter()
        printController?.presentAnimated(true, completionHandler = null)
    }
}

actual fun createPdfReportPrinter(): PdfReportPrinter = PdfReportPrinter()
