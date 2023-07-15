package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.data.local.model.GradeName
import com.hussienFahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienFahmy.core.domain.grades.use_case.GetGradeByPoints
import com.hussienFahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.semester_subjctets_domain.model.Grade
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
     * @param list List<Pair<Subject, Grade?>> we could fetch it from the database using [SubjectDao]
     *  but to be able to test all cases we need to supply the list itself
     * @return CalculationResult
     */
    suspend operator fun invoke(
        list: List<Pair<Subject, Grade?>>,
    ): Result = withContext(defaultDispatcher) {
        // the user data (current academic progress)
        val academicProgress = getAcademicProgress()
        val currentTotalHours = academicProgress.creditHours
        val currentCGpa = academicProgress.cumulativeGPA
        val currentTotalPoints = currentCGpa * currentTotalHours

        // the calculation steps
        var semesterHours = 0.0
        var semesterPointsBigDecimal = BigDecimal("0.0")
        if (list.isEmpty()) return@withContext Result.Failed(UiText.StringResource(R.string.err_waiting_to_add_subjects))

        list.forEach { (subject, grade) ->
            semesterHours += subject.creditHours
            if (grade != null) {
                semesterPointsBigDecimal += BigDecimal("${grade.points * subject.creditHours}")
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
                    grade = semesterGrade.metaData,
                    percentage = (100.0 * semesterGPA / gpaSystemNumber).toFloat()
                ),
                cumulative = Result.Success.Data(
                    gpa = cumulativeGPA,
                    grade = cumulativeGrade.metaData,
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

        data class Failed(val message: UiText) : Result()
    }
}