package com.hussienfahmy.core.domain.grades.di

import com.hussienfahmy.core.domain.grades.use_case.GetActiveGradeNames
import com.hussienfahmy.core.domain.grades.use_case.GetActiveGrades
import com.hussienfahmy.core.domain.grades.use_case.GetGradeByPoints
import org.koin.dsl.module

val coreGradesDomainModule = module {
    single { GetActiveGrades(get()) }
    single { GetActiveGradeNames(get()) }
    single { GetGradeByPoints(get()) }
}