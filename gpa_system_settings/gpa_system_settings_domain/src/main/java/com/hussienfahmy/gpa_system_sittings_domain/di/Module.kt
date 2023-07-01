package com.hussienfahmy.gpa_system_sittings_domain.di

import com.hussienFahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienfahmy.gpa_system_sittings_domain.use_case.ObserveGPASettings
import com.hussienfahmy.gpa_system_sittings_domain.use_case.UpdateGPASystem
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
    fun provideObserveGpaSettings(
        repository: GPASettingsRepository
    ): ObserveGPASettings = ObserveGPASettings(repository)

    @Provides
    @ViewModelScoped
    fun provideUpdateGPASystem(
        repository: GPASettingsRepository
    ): UpdateGPASystem = UpdateGPASystem(repository)
}