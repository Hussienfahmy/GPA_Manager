package com.hussienfahmy.grades_setting_domain.di

import com.hussienfahmy.grades_setting_domain.use_case.ActivateGrade
import com.hussienfahmy.grades_setting_domain.use_case.GetGradeByName
import com.hussienfahmy.grades_setting_domain.use_case.GradeSettingsUseCases
import com.hussienfahmy.grades_setting_domain.use_case.LoadGrades
import com.hussienfahmy.grades_setting_domain.use_case.UpdatePercentage
import com.hussienfahmy.grades_setting_domain.use_case.UpdatePoints
import org.koin.dsl.module

val gradesSettingDomainModule = module {
    single {
        GradeSettingsUseCases(
            loadGrades = LoadGrades(get()),
            updatePoints = UpdatePoints(get()),
            updatePercentage = UpdatePercentage(get()),
            activateGrade = ActivateGrade(get(), get(), GetGradeByName(get())),
        )
    }
}
