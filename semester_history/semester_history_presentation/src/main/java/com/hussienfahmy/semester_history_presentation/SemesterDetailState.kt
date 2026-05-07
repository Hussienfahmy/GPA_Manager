package com.hussienfahmy.semester_history_presentation

import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.semester_history_domain.model.SemesterDetail

data class SemesterDetailState(
    val detail: SemesterDetail? = null,
    val availableGrades: List<Grade> = emptyList(),
    val isSubmitting: Boolean = false,
    val subjectSettings: SubjectSettings? = null,
)
