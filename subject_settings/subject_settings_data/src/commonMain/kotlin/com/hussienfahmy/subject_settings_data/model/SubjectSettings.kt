package com.hussienfahmy.subject_settings_data.model

import kotlinx.serialization.Serializable

@Serializable
data class SubjectSettings(
    val subjectsMarksDependsOn: SubjectsMarksDependsOn = SubjectsMarksDependsOn.CREDIT,
    val constantMarks: Double = 100.0,
    val marksPerCreditHour: Double = 50.0,
) {
    enum class SubjectsMarksDependsOn {
        CREDIT, CONSTANT
    }
}