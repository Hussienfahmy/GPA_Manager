package com.hussienfahmy.semester_subjctets_presentaion

import com.hussienFahmy.core.data.local.model.GradeName

sealed class SemesterSubjectsEvent {
    object CLearAll : SemesterSubjectsEvent()
    data class ClearGrade(val subjectId: Long) : SemesterSubjectsEvent()
    data class DeleteSubject(val subjectId: Long) : SemesterSubjectsEvent()
    data class AddSubject(val subjectName: String, val creditHours: String) :
        SemesterSubjectsEvent()

    data class UpdateName(val subjectId: Long, val subjectName: String) : SemesterSubjectsEvent()
    data class SetGrade(val subjectId: Long, val gradeName: GradeName) : SemesterSubjectsEvent()

    object ChangeMode : SemesterSubjectsEvent()
    data class SubmitPredictiveData(
        val targetCumulativeGPA: String,
        val reserveSubjects: Boolean
    ) : SemesterSubjectsEvent()

    data class FixGrade(val subjectId: Long, val fixed: Boolean) : SemesterSubjectsEvent()
}