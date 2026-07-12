package com.hussienfahmy.semester_history_domain.model

import com.hussienfahmy.core.data.local.entity.Subject

data class SemesterDetail(
    val semester: Semester,
    val subjects: List<Subject>,
) {
    val subjectCount: Int get() = subjects.size
}