package com.hussienfahmy.semester_subjctets_presentaion.di

import com.hussienfahmy.semester_subjctets_presentaion.SemesterSubjectsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val semesterSubjectsPresentationModule = module {
    viewModel { SemesterSubjectsViewModel(get(), get(), get(), get()) }
}