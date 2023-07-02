package com.hussienfahmy.semester_marks_domain.use_case

data class SemesterMarksUseCases(
    val continuesCalculation: ContinuesCalculation,
    val resetMarks: ResetMarks,
    val setOralAvailable: SetOralAvailable,
    val setPracticalAvailable: SetPracticalAvailable,
    val setMidtermAvailable: SetMidtermAvailable,
    val changeMidtermMarks: ChangeMidtermMarks,
    val changeOralMarks: ChangeOralMarks,
    val changePracticalMarks: ChangePracticalMarks,
)