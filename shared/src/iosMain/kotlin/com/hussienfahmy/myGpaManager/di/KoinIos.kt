package com.hussienfahmy.myGpaManager.di

import com.hussienfahmy.core.data.local.di.databaseModule
import com.hussienfahmy.core.di.coreModule
import com.hussienfahmy.core.domain.analytics.di.analyticsModule
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core.domain.grades.di.coreGradesDomainModule
import com.hussienfahmy.core.domain.subject_settings.di.coreSubjectSettingsDomainModule
import com.hussienfahmy.core.domain.user_data.di.coreUserDataDomainModule
import com.hussienfahmy.core.util.PlatformContext
import com.hussienfahmy.core_ui.domain.di.coreUiDomainModule
import com.hussienfahmy.core_ui.presentation.util.initCoilImageLoader
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
import org.koin.core.context.startKoin
import org.koin.dsl.module

// The iOS analog of :app's GPAManagerApplication.onCreate() - called once from the iosApp Xcode
// project's entry point (iosApp/iosApp/iOSApp.swift) before any Compose UI is shown. Mirrors
// GPAManagerApplication's module list minus the Android-only pieces: appKoinModule (WorkManager -
// background-sync scheduling has no iOS equivalent wired up yet, see syncWorkerModule's comment)
// and firebaseModule (androidx.credentials-based Google Sign-In; iOS's AuthService binds to
// IosAuthService instead, wrapping the already-portable AuthRepository - see IosAuthService.kt).
//
// Named doInitKoin(), not initKoin(): Swift's Objective-C bridge treats any exported method whose
// name starts with "init" as an initializer, which would otherwise mangle how this is callable
// from Swift - the same reason JetBrains' own KMP project template avoids "init"-prefixed names.
fun doInitKoin() {
    initCoilImageLoader()

    startKoin {
        modules(
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
            module {
                single<AuthService> { IosAuthService(get()) }
                single { object : PlatformContext() {} }
            },
        )
    }
}
