package com.hussienFahmy.grades_setting_domain.di

import com.hussienFahmy.core.data.local.GradeDao
import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.grades_setting_domain.use_case.ActivateGrade
import com.hussienFahmy.grades_setting_domain.use_case.GetGradeByName
import com.hussienFahmy.grades_setting_domain.use_case.GradeSettingsUseCases
import com.hussienFahmy.grades_setting_domain.use_case.LoadGrades
import com.hussienFahmy.grades_setting_domain.use_case.UpdatePercentage
import com.hussienFahmy.grades_setting_domain.use_case.UpdatePoints
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
    fun provideGradeSettingsUseCases(
        subjectDao: SubjectDao,
        gradeDao: GradeDao
    ): GradeSettingsUseCases {
        return GradeSettingsUseCases(
            loadGrades = LoadGrades(gradeDao),
            updatePoints = UpdatePoints(gradeDao),
            updatePercentage = UpdatePercentage(gradeDao),
            activateGrade = ActivateGrade(gradeDao, subjectDao, GetGradeByName(gradeDao)),
        )
    }
}