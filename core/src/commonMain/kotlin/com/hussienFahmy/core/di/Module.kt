package com.hussienfahmy.core.di

import com.hussienfahmy.core.domain.report.PdfReportPrinter
import com.hussienfahmy.core.domain.report.ReportBrandingProvider
import com.hussienfahmy.core.domain.report.ReportTemplateRegistry
import com.hussienfahmy.core.domain.report.templates.DashboardRenderer
import com.hussienfahmy.core.domain.report.templates.GazetteRenderer
import com.hussienfahmy.core.domain.report.templates.LedgerRenderer
import com.hussienfahmy.core.domain.report.templates.MinimalRenderer
import com.hussienfahmy.core.domain.report.templates.ModernRenderer
import com.hussienfahmy.core.domain.report.templates.TimelineRenderer
import com.hussienfahmy.core.domain.sync.SemesterDirtyTracker
import com.hussienfahmy.core.util.ImageThumbnailer
import com.hussienfahmy.core.util.UrlOpener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

object CoreQualifiers {
    const val DEFAULT_DISPATCHER = "defaultDispatcher"
}

val coreModule = module {
    single<CoroutineDispatcher>(named(CoreQualifiers.DEFAULT_DISPATCHER)) { Dispatchers.Default }
    single<CoroutineScope> { CoroutineScope(Dispatchers.Main + SupervisorJob()) }
    single { SemesterDirtyTracker() }
    single { ImageThumbnailer(get()) }
    single { ReportBrandingProvider(get(), get()) }
    single { PdfReportPrinter(get()) }
    single { UrlOpener(get()) }

    single {
        ReportTemplateRegistry(
            listOf(
                LedgerRenderer(),
                ModernRenderer(),
                GazetteRenderer(),
                DashboardRenderer(),
                TimelineRenderer(),
                MinimalRenderer(),
            )
        )
    }
}
