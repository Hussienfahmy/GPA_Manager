package com.hussienfahmy.subject_settings_presentation.di

import com.hussienfahmy.subject_settings_presentation.SubjectsSettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val subjectSettingsPresentationModule = module {
    viewModelOf(::SubjectsSettingsViewModel)
}