package com.hussienfahmy.semester_subjctets_presentaion

import com.hussienFahmy.core.data.local.model.GradeName
import com.hussienfahmy.semester_subjctets_domain.model.Subject
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode
import com.hussienfahmy.semester_subjctets_presentaion.model.ModeResult

sealed class SemesterSubjectsState {
    object Loading : SemesterSubjectsState()
    data class Loaded(
        val subjects: List<Subject>,
        val grades: List<GradeName>,
        val mode: Mode = Mode.Normal,
        val modeResult: ModeResult,
    ) : SemesterSubjectsState()
}