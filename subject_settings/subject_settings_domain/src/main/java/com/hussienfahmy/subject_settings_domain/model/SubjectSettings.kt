package com.hussienfahmy.subject_settings_domain.model

data class SubjectSettings(
    val subjectsMarksDependsOn: SubjectsMarksDependsOn,
    val constantMarks: Double,
    val marksPerCreditHour: Double,
) {
    enum class SubjectsMarksDependsOn {
        CREDIT, CONSTANT
    }
}
