package com.hussienfahmy.semester_history_presentation

import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.semester_history_domain.model.Semester

sealed class SemesterHistoryState {
    object Loading : SemesterHistoryState()
    data class Loaded(
        val semesters: List<Semester>,
        val cumulativeGPA: Double,
        val totalCreditHours: Int,
        val currentLevel: Int,
        val currentSemester: UserData.AcademicInfo.Semester,
        val hasWorkspaceSubjects: Boolean,
    ) : SemesterHistoryState()
}
