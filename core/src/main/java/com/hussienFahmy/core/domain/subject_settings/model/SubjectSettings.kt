package com.hussienFahmy.core.domain.subject_settings.model

data class SubjectSettings(
    val subjectsMarksDependsOn: SubjectsMarksDependsOn,
    val constantMarks: Double,
    val marksPerCreditHour: Double,
) {
    enum class SubjectsMarksDependsOn {
        CREDIT, CONSTANT
    }
}
