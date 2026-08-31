package com.hussienfahmy.grades_setting_presentation.di

import com.hussienfahmy.grades_setting_presentation.GradeSettingsViewModel
import com.hussienfahmy.grades_setting_presentation.components.EditTextDialogViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val gradesSettingPresentationModule = module {
    viewModelOf(::GradeSettingsViewModel)
    viewModelOf(::EditTextDialogViewModel)
}
