package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienfahmy.semester_subjctets_domain.model.Grade
import com.hussienfahmy.semester_subjctets_domain.model.Subject
import kotlinx.coroutines.flow.map

class ObserveSubjects(
    private val subjectDao: SubjectDao,
) {
    operator fun invoke() = subjectDao.subjectsWithAssignedGrade
        .map {
            it.map { (subjectEntity, maxGrade, assignedGrade) ->
                val grade = assignedGrade?.let {
                    Grade(assignedGrade)
                }

                val maxGradeCanBeAssigned = Grade(maxGrade)

                Subject(
                    id = subjectEntity.id,
                    name = subjectEntity.name,
                    creditHours = subjectEntity.creditHours,
                    totalMarks = subjectEntity.totalMarks,
                    fixedGrade = false,
                    assignedGrade = grade,
                    maxGradeCanBeAssigned = maxGradeCanBeAssigned,
                )
            }
        }
}