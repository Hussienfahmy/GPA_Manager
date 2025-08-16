package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.core.domain.grades.use_case.GetGradeByPoints
import com.hussienfahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.myGpaManager.core.R
import com.hussienfahmy.semester_subjctets_domain.model.Subject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class Calculate(
    private val defaultDispatcher: CoroutineDispatcher,
    private val getAcademicProgress: GetAcademicProgress,
    private val getGradeByPoints: GetGradeByPoints,
    private val getGPASettings: GetGPASettings,
) {
    /**
     * calculate the given list of subjects and assigned grades
     * @param list List<Subject> we could fetch it from the database using [SubjectDao]
     *  but to be able to test all cases we need to supply the list itself
     * @return CalculationResult
     */
    suspend operator fun invoke(
        list: List<Subject>,
    ): Result = withContext(defaultDispatcher) {
        // the user data (current academic progress)
        val academicProgress = getAcademicProgress() ?: return@withContext Result.Failed(null)
        val currentTotalHours = academicProgress.creditHours
        val currentCGpa = academicProgress.cumulativeGPA
        val currentTotalPoints = currentCGpa * currentTotalHours

        // the calculation steps
        var semesterHours = 0.0
        var semesterPointsBigDecimal = BigDecimal("0.0")
        if (list.isEmpty()) return@withContext Result.Failed(UiText.StringResource(R.string.err_waiting_to_add_subjects))

        list.forEach { subject ->
            semesterHours += subject.creditHours
            if (subject.assignedGrade != null) {
                semesterPointsBigDecimal += BigDecimal("${subject.assignedGrade.points * subject.creditHours}")
            }
        }

        val semesterPoints = semesterPointsBigDecimal.toDouble()
        // semester result
        val semesterGPA = semesterPoints / semesterHours

        val newTotalHours = semesterHours + currentTotalHours
        val newTotalPoints = semesterPoints + currentTotalPoints

        val cumulativeGPA = newTotalPoints / newTotalHours

        val semesterGrade = getGradeByPoints(semesterGPA)
        val cumulativeGrade = getGradeByPoints(cumulativeGPA)

        return@withContext if (semesterGrade == null || cumulativeGrade == null) {
            Result.Failed(
                UiText.StringResource(R.string.err_calculation_failed_please_select_grade)
            )
        } else {
            val gpaSystemNumber = getGPASettings().system.number
            Result.Success(
                semester = Result.Success.Data(
                    gpa = semesterGPA,
                    grade = semesterGrade.name,
                    percentage = (100.0 * semesterGPA / gpaSystemNumber).toFloat()
                ),
                cumulative = Result.Success.Data(
                    gpa = cumulativeGPA,
                    grade = cumulativeGrade.name,
                    percentage = (100.0 * cumulativeGPA / gpaSystemNumber).toFloat()
                )
            )
        }
    }

    sealed class Result {
        data class Success(
            val semester: Data,
            val cumulative: Data,
        ) : Result() {
            data class Data(
                val gpa: Double,
                val grade: GradeName,
                val percentage: Float,
            )
        }

        data class Failed(val message: UiText?) : Result()
    }
}