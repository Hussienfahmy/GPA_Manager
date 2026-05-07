package com.hussienfahmy.semester_history_presentation

import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName

sealed interface SemesterDetailAction {
    data class OnAddSubject(
        val name: String,
        val creditHours: Double,
        val gradeName: GradeName,
        val totalMarks: Double,
        val semesterMarks: Subject.SemesterMarks?,
        val metadata: Subject.MetaData,
    ) : SemesterDetailAction

    data class OnEditSubject(
        val subject: Subject,
        val name: String,
        val creditHours: Double,
        val gradeName: GradeName,
        val totalMarks: Double,
        val semesterMarks: Subject.SemesterMarks?,
        val metadata: Subject.MetaData,
    ) : SemesterDetailAction

    data class OnDeleteSubject(val subjectId: Long) : SemesterDetailAction

    data object OnScreenExit : SemesterDetailAction
}
