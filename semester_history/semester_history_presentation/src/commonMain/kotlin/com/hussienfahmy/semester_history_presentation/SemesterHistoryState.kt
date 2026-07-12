package com.hussienfahmy.semester_history_presentation

import com.hussienfahmy.semester_history_domain.model.Semester

sealed class SemesterHistoryState {
    object Loading : SemesterHistoryState()
    data class Loaded(
        val semesters: List<Semester>,
        val cumulativeGPA: Double,
        val totalCreditHours: Int,
        val currentLevel: Int,
        val currentSemesterNum: Int, // 1 or 2
        val hasWorkspaceSubjects: Boolean,
    ) : SemesterHistoryState()
}
