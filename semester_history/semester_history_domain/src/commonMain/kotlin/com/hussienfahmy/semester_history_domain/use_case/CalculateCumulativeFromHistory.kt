package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.semester_history_domain.model.Semester

class CalculateCumulativeFromHistory {
    /**
     * Observes all archived semesters and emits the calculated cumulative GPA
     * using the weighted formula: sum(gpa * hours) / sum(hours)
     */
    operator fun invoke(semesters: List<Semester>): UserData.AcademicProgress {
        if (semesters.isEmpty()) {
            return UserData.AcademicProgress(cumulativeGPA = 0.0, creditHours = 0)
        }

        var totalPoints = 0.0
        var totalHours = 0.0

        semesters.forEach { semester ->
            totalPoints += semester.semesterGPA * semester.totalCreditHours
            totalHours += semester.totalCreditHours
        }

        val cumulativeGPA = if (totalHours == 0.0) 0.0 else totalPoints / totalHours

        return UserData.AcademicProgress(
            cumulativeGPA = cumulativeGPA,
            creditHours = totalHours.toInt(),
        )
    }
}
