package com.hussienfahmy.semester_marks_domain.di

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.di.DispatcherDefault
import com.hussienfahmy.core.domain.grades.use_case.GetActiveGrades
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object Module {

    @Provides
    @ViewModelScoped
    fun provideSemesterMarksUseCases(
        subjectDao: SubjectDao,
        getActiveGrade: GetActiveGrades,
        @DispatcherDefault defaultDispatcher: CoroutineDispatcher,
        appScope: CoroutineScope,
    ): SemesterMarksUseCases {
        return SemesterMarksUseCases(
            continuesCalculation = ContinuesCalculation(
                subjectDao = subjectDao,
                getActiveGrades = getActiveGrade,
                defaultDispatcher = defaultDispatcher,
                appScope = appScope
            ),
            resetMarks = ResetMarks(subjectDao),
            setOralAvailable = SetOralAvailable(subjectDao),
            setPracticalAvailable = SetPracticalAvailable(subjectDao),
            setMidtermAvailable = SetMidtermAvailable(subjectDao),
            setProjectAvailable = SetProjectAvailable(subjectDao),
            changeMidtermMarks = ChangeMidtermMarks(subjectDao),
            changeOralMarks = ChangeOralMarks(subjectDao),
            changePracticalMarks = ChangePracticalMarks(subjectDao),
            changeProjectMarks = ChangeProjectMarks(subjectDao),
        )
    }
}