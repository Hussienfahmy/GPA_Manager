package com.hussienfahmy.semester_subjctets_domain.di

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.di.DispatcherDefault
import com.hussienFahmy.core.di.DispatcherIO
import com.hussienFahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienFahmy.core.domain.grades.use_case.GetActiveGrades
import com.hussienFahmy.core.domain.grades.use_case.GetGradeByPoints
import com.hussienFahmy.core.domain.subject_settings.use_case.GetSubjectsSettings
import com.hussienFahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienfahmy.semester_subjctets_domain.use_case.AddSubject
import com.hussienfahmy.semester_subjctets_domain.use_case.Calculate
import com.hussienfahmy.semester_subjctets_domain.use_case.CalculationUseCases
import com.hussienfahmy.semester_subjctets_domain.use_case.ClearGrade
import com.hussienfahmy.semester_subjctets_domain.use_case.DeleteSubject
import com.hussienfahmy.semester_subjctets_domain.use_case.ObserveSubjects
import com.hussienfahmy.semester_subjctets_domain.use_case.PredictGrades
import com.hussienfahmy.semester_subjctets_domain.use_case.SetGrade
import com.hussienfahmy.semester_subjctets_domain.use_case.SubjectUseCases
import com.hussienfahmy.semester_subjctets_domain.use_case.UpdateName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object Module {

    @Provides
    @ViewModelScoped
    fun provideSetGrade(
        subjectDao: SubjectDao,
    ): SetGrade {
        return SetGrade(subjectDao)
    }

    @Provides
    @ViewModelScoped
    fun provideSubjectUseCases(
        subjectDao: SubjectDao,
        getSubjectsSettings: GetSubjectsSettings,
        setGrade: SetGrade,
    ): SubjectUseCases {
        return SubjectUseCases(
            observeSubjectsWithGrades = ObserveSubjects(subjectDao),
            clearGrade = ClearGrade(subjectDao),
            deleteSubject = DeleteSubject(subjectDao),
            addSubject = AddSubject(subjectDao, getSubjectsSettings),
            updateName = UpdateName(subjectDao),
            setGrade = setGrade,
        )
    }

    @Provides
    @ViewModelScoped
    fun provideCalculationUseCases(
        @DispatcherDefault defaultDispatcher: CoroutineDispatcher,
        @DispatcherIO ioDispatcher: CoroutineDispatcher,
        getAcademicProgress: GetAcademicProgress,
        getGradeByPoints: GetGradeByPoints,
        getGPASettings: GetGPASettings,
        getActiveGrades: GetActiveGrades,
        setGrade: SetGrade,
    ): CalculationUseCases {
        val calculate = Calculate(
            defaultDispatcher = defaultDispatcher,
            getAcademicProgress = getAcademicProgress,
            getGradeByPoints = getGradeByPoints,
            getGPASettings = getGPASettings,
        )

        return CalculationUseCases(
            calculate = calculate,
            predictGrades = PredictGrades(
                defaultDispatcher = defaultDispatcher,
                getActiveGrades = getActiveGrades,
                calculate = calculate,
                setGrade = setGrade,
                getGPASettings = getGPASettings,
            ),
        )
    }
}