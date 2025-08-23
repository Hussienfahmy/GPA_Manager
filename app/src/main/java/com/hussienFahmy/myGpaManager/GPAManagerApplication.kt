package com.hussienfahmy.myGpaManager

import android.app.Application
import com.hussienfahmy.core.data.local.di.databaseModule
import com.hussienfahmy.core.di.coreModule
import com.hussienfahmy.core.domain.grades.di.coreGradesDomainModule
import com.hussienfahmy.core.domain.subject_settings.di.coreSubjectSettingsDomainModule
import com.hussienfahmy.core.domain.user_data.di.coreUserDataDomainModule
import com.hussienfahmy.core_ui.domain.di.coreUiDomainModule
import com.hussienfahmy.gpa_system_settings_data.di.gpaSystemSettingsDataModule
import com.hussienfahmy.gpa_system_sittings_presentaion.di.gpaSystemSettingsPresentationModule
import com.hussienfahmy.grades_setting_domain.di.gradesSettingDomainModule
import com.hussienfahmy.grades_setting_presentation.di.gradesSettingPresentationModule
import com.hussienfahmy.myGpaManager.di.appKoinModule
import com.hussienfahmy.myGpaManager.di.firebaseModule
import com.hussienfahmy.onboarding_presentation.di.onboardingPresentationModule
import com.hussienfahmy.quick_domain.di.quickDomainModule
import com.hussienfahmy.quick_presentation.di.quickPresentationModule
import com.hussienfahmy.semester_marks_domain.di.semesterMarksDomainModule
import com.hussienfahmy.semester_marks_presentaion.di.semesterMarksPresentationModule
import com.hussienfahmy.semester_subjctets_domain.di.semesterSubjectsDomainModule
import com.hussienfahmy.semester_subjctets_presentaion.di.semesterSubjectsPresentationModule
import com.hussienfahmy.subject_settings_data.di.subjectSettingsDataModule
import com.hussienfahmy.subject_settings_domain.di.subjectSettingsDomainModule
import com.hussienfahmy.subject_settings_presentation.di.subjectSettingsPresentationModule
import com.hussienfahmy.sync_data.di.syncDataModule
import com.hussienfahmy.sync_domain.di.syncDomainModule
import com.hussienfahmy.user_data_data.di.userDataDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class GPAManagerApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GPAManagerApplication)
            workManagerFactory()
            modules(
                coreModule,
                databaseModule,
                gpaSystemSettingsDataModule,
                gpaSystemSettingsPresentationModule,
                gradesSettingDomainModule,
                gradesSettingPresentationModule,
                coreUiDomainModule,
                userDataDataModule,
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
                syncDataModule,
                syncDomainModule,
                onboardingPresentationModule,
                appKoinModule,
                firebaseModule
            )
        }
    }
}