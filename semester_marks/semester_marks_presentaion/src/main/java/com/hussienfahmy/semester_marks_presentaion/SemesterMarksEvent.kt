package com.hussienfahmy.semester_marks_presentaion

sealed class SemesterMarksEvent {
    data class SetPracticalAvailability(val subjectId: Long, val isAvailable: Boolean) :
        SemesterMarksEvent()

    data class SetMidtermAvailability(val subjectId: Long, val isAvailable: Boolean) :
        SemesterMarksEvent()

    data class SetOralAvailability(val subjectId: Long, val isAvailable: Boolean) :
        SemesterMarksEvent()

    data class SetProjectAvailability(val subjectId: Long, val isAvailable: Boolean) :
        SemesterMarksEvent()

    data class ChangePracticalMark(val subjectId: Long, val mark: String) : SemesterMarksEvent()
    data class ChangeMidtermMark(val subjectId: Long, val mark: String) : SemesterMarksEvent()
    data class ChangeOralMark(val subjectId: Long, val mark: String) : SemesterMarksEvent()

    data class ChangeProjectMark(val subjectId: Long, val mark: String) : SemesterMarksEvent()

    data class ResetMarks(val subjectId: Long) : SemesterMarksEvent()

    object OnScreenExit : SemesterMarksEvent()
}