package com.hussienfahmy.semester_history_domain.di

import com.hussienfahmy.semester_history_domain.use_case.AddPastSemester
import com.hussienfahmy.semester_history_domain.use_case.AddSubjectToSemester
import com.hussienfahmy.semester_history_domain.use_case.ArchiveCurrentSemester
import com.hussienfahmy.semester_history_domain.use_case.CalculateCumulativeFromHistory
import com.hussienfahmy.semester_history_domain.use_case.CalculateSemesterGPA
import com.hussienfahmy.semester_history_domain.use_case.DeleteSemester
import com.hussienfahmy.semester_history_domain.use_case.DeleteSubjectFromSemester
import com.hussienfahmy.semester_history_domain.use_case.EditSemester
import com.hussienfahmy.semester_history_domain.use_case.EditSubjectInSemester
import com.hussienfahmy.semester_history_domain.use_case.GetSemesterDetail
import com.hussienfahmy.semester_history_domain.use_case.GetSemesterHistory
import com.hussienfahmy.semester_history_domain.use_case.GetWorkspaceSubjectCount
import com.hussienfahmy.semester_history_domain.use_case.ReorderSemester
import org.koin.dsl.module

val semesterHistoryDomainModule = module {
    single { CalculateSemesterGPA(gradeDao = get()) }
    single { CalculateCumulativeFromHistory() }
    single { GetSemesterHistory(semesterDao = get()) }
    single { GetSemesterDetail(semesterDao = get(), subjectDao = get()) }
    single { AddPastSemester(semesterDao = get()) }
    single { AddSubjectToSemester(subjectDao = get()) }
    single { EditSubjectInSemester(subjectDao = get()) }
    single { DeleteSubjectFromSemester(subjectDao = get()) }
    single { GetWorkspaceSubjectCount(subjectDao = get()) }
    single { ReorderSemester(semesterDao = get()) }
    single { DeleteSemester(semesterDao = get(), subjectDao = get()) }
    single {
        EditSemester(
            semesterDao = get(),
            subjectDao = get(),
            calculateSemesterGPA = get(),
        )
    }
    single {
        ArchiveCurrentSemester(
            semesterDao = get(),
            subjectDao = get(),
            getUserData = get(),
            updateLevel = get(),
            updateSemester = get(),
            calculateSemesterGPA = get(),
        )
    }
}
