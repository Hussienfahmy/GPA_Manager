package com.hussienfahmy.sync_domain.model

import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.domain.common.model.DomainTimestamp
import com.hussienfahmy.core.domain.gpa_settings.model.GPA
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import kotlinx.serialization.Serializable

data class Settings(
    val networkGrades: List<NetworkGrade> = listOf(),
    val calculationSettings: CalculationSettings = CalculationSettings(),
    val lastUpdate: DomainTimestamp? = null
)

// @Serializable (rather than the parent Settings) since FirebaseSettings (app module) embeds
// this and NetworkGrade directly - lastUpdate/lastUpdate's DomainTimestamp isn't serialized here,
// FirebaseSettings maps it separately.
@Serializable
data class CalculationSettings(
    val gpaSystem: GPA.System = GPA.System.FOUR,
    val subjectsMarksDependsOn: SubjectSettings.SubjectsMarksDependsOn = SubjectSettings.SubjectsMarksDependsOn.CREDIT,
    val constantMarks: Double = 100.0,
    val marksPerCreditHour: Double = 50.0,
)

@Serializable
data class NetworkGrade(
    val fullName: GradeName = GradeName.F,
    val active: Boolean = true,
    val points: Double? = null,
    val percentage: Double? = null
)

fun List<Grade>.toNetworkGrades() = map {
    NetworkGrade(
        it.name,
        it.active,
        it.points,
        it.percentage
    )
}

fun List<NetworkGrade>.toGrades() = map {
    Grade(
        it.fullName,
        it.active,
        it.points,
        it.percentage
    )
}