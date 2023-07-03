package com.hussienfahmy.subject_settings_domain.di

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienFahmy.core.domain.subject_settings.use_case.GetSubjectsSettings
import com.hussienfahmy.subject_settings_domain.use_case.ApplySettingsToSubjects
import com.hussienfahmy.subject_settings_domain.use_case.ObserveSubjectsSettings
import com.hussienfahmy.subject_settings_domain.use_case.SubjectSettingsUseCases
import com.hussienfahmy.subject_settings_domain.use_case.UpdateConstantMarks
import com.hussienfahmy.subject_settings_domain.use_case.UpdateMarksPerCreditHours
import com.hussienfahmy.subject_settings_domain.use_case.UpdateSubjectsDependsOn
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object Module {

    @Provides
    @ViewModelScoped
    fun provideSubjectSettingsUseCases(
        repository: SubjectSettingsRepository,
        subjectDao: SubjectDao,
        getSubjectsSettings: GetSubjectsSettings,
    ): SubjectSettingsUseCases {
        val applySettingsToSubjects = ApplySettingsToSubjects(subjectDao, getSubjectsSettings)
        return SubjectSettingsUseCases(
            observeSubjectSettings = ObserveSubjectsSettings(repository),
            getSubjectSettings = getSubjectsSettings,
            updateSubjectsDependsOn = UpdateSubjectsDependsOn(repository, applySettingsToSubjects),
            updateConstantMarks = UpdateConstantMarks(repository, applySettingsToSubjects),
            updateMarksPerCreditHour = UpdateMarksPerCreditHours(
                repository,
                applySettingsToSubjects
            )
        )
    }
}