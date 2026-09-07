package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.domain.gpa.CalculateWeightedCumulativeGpa
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.semester_history_domain.model.Semester

class CalculateCumulativeFromHistory(
    private val calculateWeightedCumulativeGpa: CalculateWeightedCumulativeGpa,
) {
    operator fun invoke(semesters: List<Semester>): UserData.AcademicProgress {
        val result = calculateWeightedCumulativeGpa(
            semesters.map {
                CalculateWeightedCumulativeGpa.SemesterContribution(
                    gpa = it.semesterGPA,
                    gpaCreditHours = it.gpaCreditHours,
                    totalCreditHours = it.totalCreditHours,
                )
            }
        )

        return UserData.AcademicProgress(
            cumulativeGPA = result.cumulativeGPA,
            creditHours = result.totalCreditHours,
        )
    }
}
