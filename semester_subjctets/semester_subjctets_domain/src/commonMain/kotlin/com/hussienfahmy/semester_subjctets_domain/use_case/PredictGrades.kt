package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.core.domain.grades.use_case.GetActiveGrades
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.semester_subjctets_domain.model.Grade
import com.hussienfahmy.semester_subjctets_domain.model.Subject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import com.hussienfahmy.semester_subjctets_domain.use_case.Calculate.Result as CalculationResult

/**
 * iterate over the subjects and assign the grades to them from low grade to high grade
 * until the target GPA is achieved
 * the assigned grades is finally updated to the database
 */
class PredictGrades(
    getActiveGrades: GetActiveGrades,
    appScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher,
    private val calculate: Calculate,
    private val setGrade: SetGrade,
    private val getGPASettings: GetGPASettings,
) {
    val activeGrades = getActiveGrades().map { list ->
        list.map { Grade(it) }
    }.stateIn(
        scope = appScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    suspend operator fun invoke(
        subjectWithAssignedGrades: List<Subject>,
        targetCumulativeGPA: String?,
        reverseSubjects: Boolean,
    ): Result {
        return withContext(defaultDispatcher) {
            // ------------- CHECK ----------------- //
            if (subjectWithAssignedGrades.isEmpty()) return@withContext Result.Failed(
                UiText.Resource(Res.string.err_waiting_to_add_subjects)
            )

            val target =
                targetCumulativeGPA?.toDoubleOrNull() ?: return@withContext Result.Failed(null)

            val maxGPA = getGPASettings().system.number

            if (target > maxGPA)
                return@withContext Result.Failed(
                    UiText.Resource(Res.string.above_max)
                )

            // -------------- PREPARE ----------------//
            val strippedSubjectsWithGrades =
                stripAssignedGradesIfNotFixed(subjectWithAssignedGrades)

            // any subject with null grade (the user didn't set anything to it)
            // have to be f as initial value and we will assign after that start from D
            // that's mean the first calculation result all grades of subjects will be D
            val subjectsWithAssignedGrades =
                assignLowestGradeToSubjectsWithNullGrade(
                    strippedSubjectsWithGrades,
                    activeGrades.value
                )

            // The assignment loop raises subjects one grade step per pass, in list
            // order — subjects earlier in the list end up one step higher when the
            // target is hit mid-pass. "Prefer high grades on low credit-hour
            // subjects" therefore means: low credit hours first.
            val list = if (reverseSubjects) {
                subjectsWithAssignedGrades.sortedBy { it.creditHours }.toMutableList()
            } else subjectsWithAssignedGrades

            // reversed to have a list containing (d, c, c+, b-, b, b+, a-, a, a+)
            //  as we want to assign the grade from the lower to higher to get the minimum grades needed
            val activeGradesWithoutF = activeGrades.value.filter {
                it.name != GradeName.F
            }.reversed()

            // ----------------- BEGIN -----------------//
            activeGradesWithoutF.forEach { grade ->
                list.forEachIndexed { index, subject ->
                    // if the user set a fixed grade so we will not change it an go to the next subject
                    if (subject.fixedGrade) return@forEachIndexed

                    // if the grade we will assign is higher than the max grade can be achieved
                    //  will go to the next subject
                    val maxGradeName = subject.maxGradeNameCanBeAssigned
                    if (grade.name.ordinal > maxGradeName.ordinal) return@forEachIndexed

                    // assign the grade to the subject
                    list[index] =
                        list[index].copy(assignedGrade = grade)

                    when (val assignGradeResult =
                        list.isTargetCumulativeGPAAchieved(target)) {
                        Result.TargetAchieved -> {
                            // if the target GPA is achieved we will stop the loop and save the grades
                            list.saveGrades()
                            return@withContext Result.TargetAchieved
                        }

                        is Result.Failed -> {
                            return@withContext Result.Failed(assignGradeResult.message)
                        }

                        Result.TargetNotAchieved -> {}
                    }
                }
            }
            // the loop ends without an in-loop hit — every subject was fixed or capped,
            // so no assignment triggered a check. The grades on hand may still meet the
            // target (e.g. all subjects fixed at high grades), so check once more.
            list.saveGrades()
            when (list.isTargetCumulativeGPAAchieved(target)) {
                Result.TargetAchieved -> Result.TargetAchieved
                else -> Result.TargetNotAchieved
            }
        }
    }

    private fun stripAssignedGradesIfNotFixed(list: List<Subject>): List<Subject> {
        return list.map { subject ->
            if (subject.fixedGrade) {
                subject
            } else subject.copy(assignedGrade = null)
        }.toMutableList()
    }

    private fun assignLowestGradeToSubjectsWithNullGrade(
        list: List<Subject>,
        activeGrades: List<Grade>
    ) = list.map { subject ->
        val grade = subject.assignedGrade
        if (grade == null) {
            subject.copy(assignedGrade = activeGrades.minBy { it.points })
        } else {
            subject
        }
    }.toMutableList()

    private suspend fun MutableList<Subject>.isTargetCumulativeGPAAchieved(
        targetCumulativeGPA: Double
    ): Result {
        return when (val calculationResult = calculate(this)) {
            is CalculationResult.Success -> if (calculationResult.cumulative.gpa >= targetCumulativeGPA)
                Result.TargetAchieved else Result.TargetNotAchieved

            is CalculationResult.Failed -> Result.Failed(
                calculationResult.message
            )
        }
    }

    private suspend fun MutableList<Subject>.saveGrades() =
        forEach {
            setGrade(it.id, it.assignedGrade?.name)
        }

    sealed class Result {
        object TargetAchieved : Result()
        object TargetNotAchieved : Result()
        data class Failed(val message: UiText?) : Result()
    }
}