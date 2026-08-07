package com.hussienfahmy.core.util

// Constructor param for every :core expect class that needs a platform application handle
// (ImageThumbnailer, ReportBrandingProvider, PdfReportPrinter, UrlOpener, ...) - Android's actual
// is a typealias to android.content.Context, resolved via Koin's own androidContext() binding
// (single<Context>, registered by GPAManagerApplication.onCreate()) with no extra wiring needed.
// iOS's actual is an empty placeholder, registered as a trivial Koin single in KoinIos.kt.
// Declared abstract: android.content.Context itself is abstract, and expect/actual modality must
// match - iOS's actual instantiates it via an anonymous subclass (object : PlatformContext() {}).
expect abstract class PlatformContext
