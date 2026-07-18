package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.entity.Subject
import kotlinx.coroutines.flow.first

class CalculateSemesterGPA(
    private val gradeDao: GradeDao,
) {
    suspend operator fun invoke(subjects: List<Subject>): Double {
        val grades = gradeDao.grades.first()

        val gradedSubjects = subjects.mapNotNull { subject ->
            val grade = subject.gradeName?.let { name -> grades.find { it.name == name } }
            val points = grade?.points ?: return@mapNotNull null
            Pair(points, subject.creditHours)
        }

        val totalPoints = gradedSubjects.sumOf { (points, hours) -> points * hours }
        val totalHours = gradedSubjects.sumOf { it.second }

        return if (totalHours == 0.0) 0.0 else totalPoints / totalHours
    }
}
