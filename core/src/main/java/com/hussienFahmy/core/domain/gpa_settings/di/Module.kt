package com.hussienfahmy.core.domain.gpa_settings.di

import com.hussienfahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideGetGpaSettings(
        repository: GPASettingsRepository
    ): GetGPASettings = GetGPASettings(repository)
}