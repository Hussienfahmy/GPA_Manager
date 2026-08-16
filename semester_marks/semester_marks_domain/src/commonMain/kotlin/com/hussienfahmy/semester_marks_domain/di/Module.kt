package com.hussienfahmy.semester_marks_domain.di

import com.hussienfahmy.core.di.CoreQualifiers
import com.hussienfahmy.semester_marks_domain.use_case.ChangeFinalExamMaxMarks
import com.hussienfahmy.semester_marks_domain.use_case.ChangeMidtermMarks
import com.hussienfahmy.semester_marks_domain.use_case.ChangeOralMarks
import com.hussienfahmy.semester_marks_domain.use_case.ChangePracticalMarks
import com.hussienfahmy.semester_marks_domain.use_case.ChangeProjectMarks
import com.hussienfahmy.semester_marks_domain.use_case.ContinuesCalculation
import com.hussienfahmy.semester_marks_domain.use_case.ResetMarks
import com.hussienfahmy.semester_marks_domain.use_case.SemesterMarksUseCases
import com.hussienfahmy.semester_marks_domain.use_case.SetMidtermAvailable
import com.hussienfahmy.semester_marks_domain.use_case.SetOralAvailable
import com.hussienfahmy.semester_marks_domain.use_case.SetPracticalAvailable
import com.hussienfahmy.semester_marks_domain.use_case.SetProjectAvailable
import com.hussienfahmy.semester_marks_domain.use_case.SyncGradeWithMarks
import org.koin.core.qualifier.named
import org.koin.dsl.module

val semesterMarksDomainModule = module {
    single {
        SemesterMarksUseCases(
            continuesCalculation = ContinuesCalculation(
                subjectDao = get(),
                getActiveGrades = get(),
                defaultDispatcher = get(named(CoreQualifiers.DEFAULT_DISPATCHER)),
                appScope = get()
            ),
            resetMarks = ResetMarks(get(), get()),
            setOralAvailable = SetOralAvailable(get(), get()),
            setPracticalAvailable = SetPracticalAvailable(get(), get()),
            setMidtermAvailable = SetMidtermAvailable(get(), get()),
            setProjectAvailable = SetProjectAvailable(get(), get()),
            changeMidtermMarks = ChangeMidtermMarks(get(), get()),
            changeOralMarks = ChangeOralMarks(get(), get()),
            changePracticalMarks = ChangePracticalMarks(get(), get()),
            changeProjectMarks = ChangeProjectMarks(get(), get()),
            syncGradeWithMarks = SyncGradeWithMarks(get(), get(), get()),
            changeFinalExamMaxMarks = ChangeFinalExamMaxMarks(get(), get()),
        )
    }
}