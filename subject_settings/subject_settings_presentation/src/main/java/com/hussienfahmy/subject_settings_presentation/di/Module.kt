package com.hussienfahmy.subject_settings_presentation.di

import com.hussienfahmy.subject_settings_presentation.SubjectsSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val subjectSettingsPresentationModule = module {
    viewModel { SubjectsSettingsViewModel(get(), get()) }
}