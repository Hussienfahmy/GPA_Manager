package com.hussienfahmy.subject_settings_domain.di

import com.hussienfahmy.subject_settings_domain.use_case.ApplySettingsToSubjects
import com.hussienfahmy.subject_settings_domain.use_case.ObserveSubjectsSettings
import com.hussienfahmy.subject_settings_domain.use_case.SubjectSettingsUseCases
import com.hussienfahmy.subject_settings_domain.use_case.UpdateConstantMarks
import com.hussienfahmy.subject_settings_domain.use_case.UpdateMarksPerCreditHours
import com.hussienfahmy.subject_settings_domain.use_case.UpdateSubjectsDependsOn
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val subjectSettingsDomainModule = module {
    singleOf(::ApplySettingsToSubjects)
    singleOf(::ObserveSubjectsSettings)
    singleOf(::UpdateSubjectsDependsOn)
    singleOf(::UpdateConstantMarks)
    singleOf(::UpdateMarksPerCreditHours)
    singleOf(::SubjectSettingsUseCases)
}