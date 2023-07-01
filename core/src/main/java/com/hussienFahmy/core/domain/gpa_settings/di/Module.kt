package com.hussienFahmy.core.domain.gpa_settings.di

import com.hussienFahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienFahmy.core.domain.gpa_settings.use_case.GetGPASettings
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
    fun provideGetGpaSettings(
        repository: GPASettingsRepository
    ): GetGPASettings = GetGPASettings(repository)
}