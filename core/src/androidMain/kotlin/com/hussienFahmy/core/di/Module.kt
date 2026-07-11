package com.hussienfahmy.core.di

import com.hussienfahmy.core.domain.report.ReportTemplateRegistry
import com.hussienfahmy.core.domain.report.templates.DashboardRenderer
import com.hussienfahmy.core.domain.report.templates.GazetteRenderer
import com.hussienfahmy.core.domain.report.templates.LedgerRenderer
import com.hussienfahmy.core.domain.report.templates.MinimalRenderer
import com.hussienfahmy.core.domain.report.templates.ModernRenderer
import com.hussienfahmy.core.domain.report.templates.TimelineRenderer
import com.hussienfahmy.core.domain.sync.SemesterDirtyTracker
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
