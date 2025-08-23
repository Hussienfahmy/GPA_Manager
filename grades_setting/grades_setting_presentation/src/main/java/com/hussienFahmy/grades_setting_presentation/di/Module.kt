package com.hussienfahmy.grades_setting_presentation.di

import com.hussienfahmy.grades_setting_presentation.GradeSettingsViewModel
import com.hussienfahmy.grades_setting_presentation.components.EditTextDialogViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val gradesSettingPresentationModule = module {
    viewModel { GradeSettingsViewModel(get()) }
    viewModel { EditTextDialogViewModel(get()) }
}
