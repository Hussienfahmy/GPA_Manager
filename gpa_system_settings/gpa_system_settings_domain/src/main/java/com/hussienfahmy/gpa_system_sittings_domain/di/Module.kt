package com.hussienfahmy.gpa_system_sittings_domain.di

import com.hussienFahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienFahmy.core.domain.gpa_settings.use_case.UpdateGPASystem
import com.hussienfahmy.gpa_system_sittings_domain.use_case.ObserveGPASettings
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
    fun provideObserveGpaSettings(
        repository: GPASettingsRepository
    ): ObserveGPASettings = ObserveGPASettings(repository)

    @Provides
    @Singleton
    fun provideUpdateGPASystem(
        repository: GPASettingsRepository
    ): UpdateGPASystem = UpdateGPASystem(repository)
}