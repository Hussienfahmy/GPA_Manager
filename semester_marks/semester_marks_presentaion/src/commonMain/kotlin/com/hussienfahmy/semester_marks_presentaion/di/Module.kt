package com.hussienfahmy.semester_marks_presentaion.di

import com.hussienfahmy.semester_marks_presentaion.SemesterMarksViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val semesterMarksPresentationModule = module {
    viewModelOf(::SemesterMarksViewModel)
}