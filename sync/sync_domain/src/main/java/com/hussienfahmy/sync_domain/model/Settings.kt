package com.hussienfahmy.sync_domain.model

import androidx.annotation.Keep
import com.hussienFahmy.core.data.local.entity.Grade
import com.hussienFahmy.core.data.local.model.GradeName
import com.hussienFahmy.core.domain.gpa_settings.model.GPA
import com.hussienFahmy.core.domain.subject_settings.model.SubjectSettings

@Keep
data class Settings(
    val networkGrades: List<NetworkGrade> = listOf(),
    val calculationSettings: CalculationSettings = CalculationSettings()
)

data class CalculationSettings(
    val gpaSystem: GPA.System = GPA.System.FOUR,
    val subjectsMarksDependsOn: SubjectSettings.SubjectsMarksDependsOn = SubjectSettings.SubjectsMarksDependsOn.CREDIT,
    val constantMarks: Double = 100.0,
    val marksPerCreditHour: Double = 50.0,
)

data class NetworkGrade(
    val fullName: GradeName = GradeName.F,
    val active: Boolean = true,
    val points: Double? = null,
    val percentage: Double? = null
)

fun List<Grade>.toNetworkGrades() = map {
    NetworkGrade(
        it.metaData,
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