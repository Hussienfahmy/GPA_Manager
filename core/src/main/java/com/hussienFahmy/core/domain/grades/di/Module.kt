package com.hussienFahmy.core.domain.grades.di

import com.hussienFahmy.core.data.local.GradeDao
import com.hussienFahmy.core.domain.grades.use_case.GetActiveGradeNames
import com.hussienFahmy.core.domain.grades.use_case.GetActiveGrades
import com.hussienFahmy.core.domain.grades.use_case.GetGradeByPoints
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
    fun provideGetActiveGrades(gradeDao: GradeDao) = GetActiveGrades(gradeDao)

    @Provides
    @ViewModelScoped
    fun provideGetActiveGradeNames(gradeDao: GradeDao) = GetActiveGradeNames(gradeDao)

    @Provides
    @ViewModelScoped
    fun provideGetGradeByPoints(gradeDao: GradeDao) = GetGradeByPoints(gradeDao)
}