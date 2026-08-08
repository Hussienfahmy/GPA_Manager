package com.hussienfahmy.core.util

// Constructor param for platform-handle-needing :core classes (ImageThumbnailer, PdfReportPrinter,
// UrlOpener, ...). Android's actual is a typealias to Context - abstract, so this must be abstract
// too (expect/actual modality must match). iOS's actual instantiates it via object : PlatformContext() {}.
expect abstract class PlatformContext
