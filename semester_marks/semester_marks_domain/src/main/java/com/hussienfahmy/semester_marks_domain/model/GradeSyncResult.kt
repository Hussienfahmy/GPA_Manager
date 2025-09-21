package com.hussienfahmy.semester_marks_domain.model

import com.hussienfahmy.core.data.local.entity.Grade

sealed class GradeSyncResult {
    object NoChangeNeeded : GradeSyncResult()

    data class DowngradeRequired(
        val subjectId: Long,
        val fromGrade: Grade,
        val toGrade: Grade,
    ) : GradeSyncResult()

    data class Error(
        val message: String
    ) : GradeSyncResult()
}