package com.hussienfahmy.semester_marks_presentaion.di

import com.hussienfahmy.semester_marks_presentaion.SemesterMarksViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val semesterMarksPresentationModule = module {
    viewModel { SemesterMarksViewModel(get()) }
}