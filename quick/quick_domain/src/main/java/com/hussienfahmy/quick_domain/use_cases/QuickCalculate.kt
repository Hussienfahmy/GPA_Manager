package com.hussienfahmy.quick_domain.use_cases

import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.quick_domain.model.QuickCalculationRequest
import kotlin.math.floor

class QuickCalculate(
    private val getGPASettings: GetGPASettings,
) {
    suspend operator fun invoke(
        request: QuickCalculationRequest,
    ): Result {
        // Checks
        val maxGPA = getGPASettings().system.number

        val cumulativeGPA = request.cumulativeGPA.toDoubleOrNull()
            ?: return Result.InValidCumulativeGPA

        val totalHours = request.totalHours.toIntOrNull()
            ?: return Result.InValidTotalHours

        val semesterGPA = request.semesterGPA.toDoubleOrNull()
            ?: return Result.InValidSemesterGPA

        val semesterHours = request.semesterHours.toIntOrNull()
            ?: return Result.InValidSemesterHours

        if (cumulativeGPA > maxGPA) return Result.CumulativeGPAAboveMax
        if (semesterGPA > maxGPA) return Result.SemesterGPAAboveMax

        // Calculate
        val currentTotalPoint = cumulativeGPA * totalHours
        val semesterPoints = semesterGPA * semesterHours

        val newTotalPoints = currentTotalPoint + semesterPoints
        val newTotalHours = totalHours + semesterHours

        // can't divide by zero
        if (newTotalHours == 0) return Result.TotalHoursIsZero

        val newCumulativeGPA = (floor((newTotalPoints / newTotalHours) * 100) / 100).toFloat()

        return Result.Success(newCumulativeGPA)
    }

    sealed class Result {
        data class Success(val newCumulativeGPA: Float) : Result()
        object InValidCumulativeGPA : Result()
        object CumulativeGPAAboveMax : Result()
        object InValidTotalHours : Result()
        object InValidSemesterGPA : Result()
        object SemesterGPAAboveMax : Result()
        object InValidSemesterHours : Result()
        object TotalHoursIsZero : Result()
    }
}