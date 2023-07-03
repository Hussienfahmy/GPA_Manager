package com.hussienFahmy.core.domain.subject_settings.di

import com.hussienFahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienFahmy.core.domain.subject_settings.use_case.GetSubjectsSettings
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
    fun provideGetSubjectsSettings(
        repository: SubjectSettingsRepository,
    ): GetSubjectsSettings {
        return GetSubjectsSettings(repository)
    }
}