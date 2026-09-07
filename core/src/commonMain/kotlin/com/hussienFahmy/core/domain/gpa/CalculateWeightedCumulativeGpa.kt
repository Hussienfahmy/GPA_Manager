package com.hussienfahmy.core.domain.gpa

// Shared by History and sync push, weighted by gpaCreditHours not totalCreditHours.
class CalculateWeightedCumulativeGpa {

    data class SemesterContribution(
        val gpa: Double,
        val gpaCreditHours: Int,
        val totalCreditHours: Int,
    )

    data class Result(
        val cumulativeGPA: Double,
        val totalCreditHours: Int,
    )

    operator fun invoke(semesters: List<SemesterContribution>): Result {
        if (semesters.isEmpty()) return Result(cumulativeGPA = 0.0, totalCreditHours = 0)

        var totalPoints = 0.0
        var gpaHours = 0
        var totalHours = 0

        semesters.forEach { semester ->
            totalPoints += semester.gpa * semester.gpaCreditHours
            gpaHours += semester.gpaCreditHours
            totalHours += semester.totalCreditHours
        }

        val cumulativeGPA = if (gpaHours == 0) 0.0 else totalPoints / gpaHours

        return Result(cumulativeGPA = cumulativeGPA, totalCreditHours = totalHours)
    }
}
