package com.hussienfahmy.semester_marks_presentaion

import com.hussienfahmy.semester_marks_domain.model.Subject

sealed class SemesterMarksState {
    object Loading : SemesterMarksState()
    data class Calculated(val subjects: List<Subject>) : SemesterMarksState()
}