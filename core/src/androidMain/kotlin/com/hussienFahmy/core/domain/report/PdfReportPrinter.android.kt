package com.hussienfahmy.core.domain.report

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import org.koin.core.context.GlobalContext

actual class PdfReportPrinter(private val context: Context) {
    actual fun print(html: String, title: String) {
        val webView = WebView(context.applicationContext)
        webView.settings.javaScriptEnabled = false
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String?) {
                val adapter = view.createPrintDocumentAdapter(title)
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
                val attrs = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                    .build()
                printManager.print(title, adapter, attrs)
                view.webViewClient = WebViewClient()
            }
        }
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }
}

// Same GlobalContext pattern as the other :core factories (getDatabaseBuilder,
// createImageThumbnailer) - the expect fun signature is shared with the future iOS actual.
actual fun createPdfReportPrinter(): PdfReportPrinter =
    PdfReportPrinter(GlobalContext.get().get())
