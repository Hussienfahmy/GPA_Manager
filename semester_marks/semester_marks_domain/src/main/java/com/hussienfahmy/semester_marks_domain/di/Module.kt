package com.hussienfahmy.semester_marks_domain.di

import com.hussienFahmy.core.data.local.GradeDao
import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.di.DispatcherDefault
import com.hussienFahmy.core.di.DispatcherIO
import com.hussienfahmy.semester_marks_domain.use_case.ChangeMidtermMarks
import com.hussienfahmy.semester_marks_domain.use_case.ChangeOralMarks
import com.hussienfahmy.semester_marks_domain.use_case.ChangePracticalMarks
import com.hussienfahmy.semester_marks_domain.use_case.ContinuesCalculation
import com.hussienfahmy.semester_marks_domain.use_case.ResetMarks
import com.hussienfahmy.semester_marks_domain.use_case.SemesterMarksUseCases
import com.hussienfahmy.semester_marks_domain.use_case.SetMidtermAvailable
import com.hussienfahmy.semester_marks_domain.use_case.SetOralAvailable
import com.hussienfahmy.semester_marks_domain.use_case.SetPracticalAvailable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object Module {

    @Provides
    @ViewModelScoped
    fun provideSemesterMarksUseCases(
        subjectDao: SubjectDao,
        gradeDao: GradeDao,
        @DispatcherIO ioDispatcher: CoroutineDispatcher,
        @DispatcherDefault defaultDispatcher: CoroutineDispatcher,
    ): SemesterMarksUseCases {
        return SemesterMarksUseCases(
            continuesCalculation = ContinuesCalculation(subjectDao, gradeDao, defaultDispatcher),
            resetMarks = ResetMarks(subjectDao, ioDispatcher),
            setOralAvailable = SetOralAvailable(subjectDao, ioDispatcher),
            setPracticalAvailable = SetPracticalAvailable(subjectDao, ioDispatcher),
            setMidtermAvailable = SetMidtermAvailable(subjectDao, ioDispatcher),
            changeMidtermMarks = ChangeMidtermMarks(subjectDao, ioDispatcher),
            changeOralMarks = ChangeOralMarks(subjectDao, ioDispatcher),
            changePracticalMarks = ChangePracticalMarks(subjectDao, ioDispatcher),
        )
    }
}