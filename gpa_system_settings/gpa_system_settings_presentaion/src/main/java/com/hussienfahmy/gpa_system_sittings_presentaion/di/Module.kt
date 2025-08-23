package com.hussienfahmy.gpa_system_sittings_presentaion.di

import com.hussienfahmy.gpa_system_sittings_presentaion.GPASettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val gpaSystemSettingsPresentationModule = module {
    viewModel { GPASettingsViewModel(get(), get()) }
}
