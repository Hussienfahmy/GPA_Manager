package com.hussienfahmy.gpa_system_settings_data.di

import com.hussienfahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.core.domain.gpa_settings.use_case.UpdateGPASystem
import com.hussienfahmy.gpa_system_settings_data.datastore.GPADatastore
import com.hussienfahmy.gpa_system_settings_data.repository.GPASettingsRepositoryImpl
import com.hussienfahmy.gpa_system_sittings_domain.use_case.ObserveGPASettings
import org.koin.core.module.Module
import org.koin.dsl.module

// Koin module for GPA System Settings Data
val gpaSystemSettingsDataModule: Module = module {
    single { GPADatastore(get()) }

    // Repository binding
    single<GPASettingsRepository> { GPASettingsRepositoryImpl(get()) }

    // Use cases
    single { GetGPASettings(get()) }
    single { ObserveGPASettings(get()) }
    single { UpdateGPASystem(get()) }
}
