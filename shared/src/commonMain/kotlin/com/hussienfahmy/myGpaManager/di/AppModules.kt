package com.hussienfahmy.myGpaManager.di

import com.hussienfahmy.core.data.local.di.databaseModule
import com.hussienfahmy.core.di.coreModule
import com.hussienfahmy.core.domain.analytics.di.analyticsModule
import com.hussienfahmy.core.domain.crash.di.crashReporterModule
import com.hussienfahmy.core.domain.grades.di.coreGradesDomainModule
import com.hussienfahmy.core.domain.subject_settings.di.coreSubjectSettingsDomainModule
import com.hussienfahmy.core.domain.user_data.di.coreUserDataDomainModule
import com.hussienfahmy.core_ui.domain.di.coreUiDomainModule
import com.hussienfahmy.gpa_system_settings_data.di.gpaSystemSettingsDataModule
import com.hussienfahmy.gpa_system_sittings_presentaion.di.gpaSystemSettingsPresentationModule
import com.hussienfahmy.grades_setting_domain.di.gradesSettingDomainModule
import com.hussienfahmy.grades_setting_presentation.di.gradesSettingPresentationModule
import com.hussienfahmy.onboarding_presentation.di.onboardingPresentationModule
import com.hussienfahmy.quick_domain.di.quickDomainModule
import com.hussienfahmy.quick_presentation.di.quickPresentationModule
import com.hussienfahmy.semester_history_domain.di.semesterHistoryDomainModule
import com.hussienfahmy.semester_history_presentation.di.semesterHistoryPresentationModule
import com.hussienfahmy.semester_marks_domain.di.semesterMarksDomainModule
import com.hussienfahmy.semester_marks_presentaion.di.semesterMarksPresentationModule
import com.hussienfahmy.semester_subjctets_domain.di.semesterSubjectsDomainModule
import com.hussienfahmy.semester_subjctets_presentaion.di.semesterSubjectsPresentationModule
import com.hussienfahmy.subject_settings_data.di.subjectSettingsDataModule
import com.hussienfahmy.subject_settings_domain.di.subjectSettingsDomainModule
import com.hussienfahmy.subject_settings_presentation.di.subjectSettingsPresentationModule
import com.hussienfahmy.sync_domain.di.syncDomainModule
import org.koin.core.module.Module

// Common to both platform entry points (:app's GPAManagerApplication.onCreate() and :shared's
// KoinIos.kt doInitKoin()) - each calls startKoin { modules(sharedAppModules + its own
// platform-only extras) } instead of maintaining two near-identical module lists.
val sharedAppModules: List<Module> = listOf(
    coreModule,
    databaseModule,
    gpaSystemSettingsDataModule,
    gpaSystemSettingsPresentationModule,
    gradesSettingDomainModule,
    gradesSettingPresentationModule,
    coreUiDomainModule,
    coreUserDataDomainModule,
    quickDomainModule,
    quickPresentationModule,
    subjectSettingsDataModule,
    subjectSettingsDomainModule,
    subjectSettingsPresentationModule,
    coreSubjectSettingsDomainModule,
    semesterMarksDomainModule,
    semesterSubjectsDomainModule,
    semesterMarksPresentationModule,
    semesterSubjectsPresentationModule,
    coreGradesDomainModule,
    semesterHistoryDomainModule,
    semesterHistoryPresentationModule,
    syncDomainModule,
    onboardingPresentationModule,
    sharedKoinModule,
    sharedFirebaseModule,
    analyticsModule,
    crashReporterModule,
)
