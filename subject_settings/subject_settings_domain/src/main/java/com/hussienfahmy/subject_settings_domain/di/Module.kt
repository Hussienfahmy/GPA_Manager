package com.hussienfahmy.subject_settings_domain.di

import com.hussienfahmy.subject_settings_domain.use_case.ApplySettingsToSubjects
import com.hussienfahmy.subject_settings_domain.use_case.ObserveSubjectsSettings
import com.hussienfahmy.subject_settings_domain.use_case.SubjectSettingsUseCases
import com.hussienfahmy.subject_settings_domain.use_case.UpdateConstantMarks
import com.hussienfahmy.subject_settings_domain.use_case.UpdateMarksPerCreditHours
import com.hussienfahmy.subject_settings_domain.use_case.UpdateSubjectsDependsOn
import org.koin.dsl.module

val subjectSettingsDomainModule = module {
    single { ApplySettingsToSubjects(get(), get()) }

    single {
        SubjectSettingsUseCases(
            observeSubjectSettings = ObserveSubjectsSettings(get()),
            getSubjectSettings = get(),
            updateSubjectsDependsOn = UpdateSubjectsDependsOn(get(), get()),
            updateConstantMarks = UpdateConstantMarks(get(), get()),
            updateMarksPerCreditHour = UpdateMarksPerCreditHours(get(), get())
        )
    }
}