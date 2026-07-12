package com.hussienfahmy.semester_subjctets_domain.di

import com.hussienfahmy.core.di.CoreQualifiers
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
import org.koin.core.qualifier.named
import org.koin.dsl.module

val semesterSubjectsDomainModule = module {
    single { SetGrade(get()) }

    single {
        SubjectUseCases(
            observeSubjectsWithGrades = ObserveSubjects(get()),
            clearGrade = ClearGrade(get()),
            deleteSubject = DeleteSubject(get()),
            addSubject = AddSubject(get(), get()),
            updateName = UpdateName(get()),
            setGrade = get(),
        )
    }

    single {
        val calculate = Calculate(
            defaultDispatcher = get(named(CoreQualifiers.DEFAULT_DISPATCHER)),
            getAcademicProgress = get(),
            getGradeByPoints = get(),
            getGPASettings = get(),
        )

        CalculationUseCases(
            calculate = calculate,
            predictGrades = PredictGrades(
                defaultDispatcher = get(named(CoreQualifiers.DEFAULT_DISPATCHER)),
                getActiveGrades = get(),
                calculate = calculate,
                setGrade = get(),
                getGPASettings = get(),
                appScope = get()
            ),
        )
    }
}