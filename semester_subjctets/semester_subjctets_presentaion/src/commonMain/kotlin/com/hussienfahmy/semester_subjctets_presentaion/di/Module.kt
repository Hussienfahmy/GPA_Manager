package com.hussienfahmy.semester_subjctets_presentaion.di

import com.hussienfahmy.semester_subjctets_presentaion.SemesterSubjectsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val semesterSubjectsPresentationModule = module {
    viewModelOf(::SemesterSubjectsViewModel)
}