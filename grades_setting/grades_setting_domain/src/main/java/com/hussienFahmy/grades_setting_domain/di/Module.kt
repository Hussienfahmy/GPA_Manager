package com.hussienfahmy.grades_setting_domain.di

import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.grades_setting_domain.use_case.ActivateGrade
import com.hussienfahmy.grades_setting_domain.use_case.GetGradeByName
import com.hussienfahmy.grades_setting_domain.use_case.GradeSettingsUseCases
import com.hussienfahmy.grades_setting_domain.use_case.LoadGrades
import com.hussienfahmy.grades_setting_domain.use_case.UpdatePercentage
import com.hussienfahmy.grades_setting_domain.use_case.UpdatePoints
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