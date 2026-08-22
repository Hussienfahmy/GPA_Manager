package com.hussienfahmy.semester_history_presentation

sealed class SemesterHistoryEvent {
    object FinishSemester : SemesterHistoryEvent()
    data class DeleteSemester(val id: Long) : SemesterHistoryEvent()
    data class AddSummarySemester(
        val label: String,
        val semesterGPA: Double,
        val totalCreditHours: Int,
        val level: Int,
    ) : SemesterHistoryEvent()

    data class AddDetailedSemester(
        val label: String,
        val level: Int,
    ) : SemesterHistoryEvent()

    data class EditSemesterLabel(val id: Long, val label: String) : SemesterHistoryEvent()
    data class EditSummarySemester(
        val id: Long,
        val label: String,
        val semesterGPA: Double,
        val totalCreditHours: Int,
    ) : SemesterHistoryEvent()

    data class MoveSemesterUp(val id: Long) : SemesterHistoryEvent()
    data class MoveSemesterDown(val id: Long) : SemesterHistoryEvent()

    object OnScreenExit : SemesterHistoryEvent()
}
