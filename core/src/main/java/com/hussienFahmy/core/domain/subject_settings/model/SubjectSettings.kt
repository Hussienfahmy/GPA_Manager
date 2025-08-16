package com.hussienfahmy.core.domain.subject_settings.model

import androidx.annotation.Keep

@Keep
data class SubjectSettings(
    val subjectsMarksDependsOn: SubjectsMarksDependsOn,
    val constantMarks: Double,
    val marksPerCreditHour: Double,
) {
    @Keep
    enum class SubjectsMarksDependsOn {
        CREDIT, CONSTANT
    }
}
