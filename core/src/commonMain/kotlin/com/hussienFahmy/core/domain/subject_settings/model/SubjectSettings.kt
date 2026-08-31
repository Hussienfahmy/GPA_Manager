package com.hussienfahmy.core.domain.subject_settings.model

import kotlinx.serialization.Serializable

data class SubjectSettings(
    val subjectsMarksDependsOn: SubjectsMarksDependsOn,
    val constantMarks: Double,
    val marksPerCreditHour: Double,
) {
    // @Serializable needed for sync_domain.model.CalculationSettings' Firestore round-trip.
    @Serializable
    enum class SubjectsMarksDependsOn {
        CREDIT, CONSTANT
    }
}
