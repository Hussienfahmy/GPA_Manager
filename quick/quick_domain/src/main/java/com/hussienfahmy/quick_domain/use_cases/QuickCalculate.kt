package com.hussienfahmy.quick_domain.use_cases

import com.hussienfahmy.quick_domain.model.QuickCalculationRequest
import kotlin.math.floor

// todo get max gpa from user data and check for valid gpa
class QuickCalculate {
    operator fun invoke(
        request: QuickCalculationRequest,
    ): Result {
        val cumulativeGPA = request.cumulativeGPA.toDoubleOrNull()
            ?: return Result.InValidCumulativeGPA

        val totalHours = request.totalHours.toIntOrNull()
            ?: return Result.InValidTotalHours

        val semesterGPA = request.semesterGPA.toDoubleOrNull()
            ?: return Result.InValidSemesterGPA

        val semesterHours = request.semesterHours.toIntOrNull()
            ?: return Result.InValidSemesterHours

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
        object InValidTotalHours : Result()
        object InValidSemesterGPA : Result()
        object InValidSemesterHours : Result()
        object TotalHoursIsZero : Result()
    }
}