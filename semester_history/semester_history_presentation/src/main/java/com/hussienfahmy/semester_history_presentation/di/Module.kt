package com.hussienfahmy.semester_history_presentation.di

import com.hussienfahmy.semester_history_presentation.SemesterDetailViewModel
import com.hussienfahmy.semester_history_presentation.SemesterHistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val semesterHistoryPresentationModule = module {
    viewModel {
        SemesterHistoryViewModel(
            getSemesterHistory = get(),
            archiveCurrentSemester = get(),
            addPastSemester = get(),
            deleteSemester = get(),
            editSemester = get(),
            calculateCumulativeFromHistory = get(),
            reorderSemester = get(),
            getUserData = get(),
            getWorkspaceSubjectCount = get(),
            pushSemesters = get(),
            applicationScope = get(),
            dirtyTracker = get(),
            authRepository = get(),
        )
    }
    viewModel { (semesterId: Long) ->
        SemesterDetailViewModel(
            semesterId = semesterId,
            getSemesterDetail = get(),
            addSubjectToSemester = get(),
            editSubjectInSemester = get(),
            deleteSubjectFromSemester = get(),
            editSemester = get(),
            getActiveGrades = get(),
            pushSemesters = get(),
            applicationScope = get(),
            dirtyTracker = get(),
            authRepository = get(),
        )
    }
}
