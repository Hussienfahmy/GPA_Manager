package com.hussienfahmy.core.domain.report

import com.hussienfahmy.core.util.PlatformContext

expect class PdfReportPrinter(context: PlatformContext) {
    fun print(html: String, title: String)
}
