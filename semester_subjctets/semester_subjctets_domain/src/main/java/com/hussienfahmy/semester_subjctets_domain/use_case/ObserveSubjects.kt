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
            it.map { (subjectEntity, gradeEntity) ->
                val subject = Subject(
                    id = subjectEntity.id,
                    name = subjectEntity.name,
                    creditHours = subjectEntity.creditHours,
                    totalMarks = subjectEntity.totalMarks,
                    fixedGrade = false,
                    selectedGradeName = subjectEntity.gradeName,
                    maxGradeNameCanBeAssigned = subjectEntity.metadata.maxGradeNameCanAchieve,
                )

                val grade = gradeEntity?.let {
                    Grade(gradeEntity)
                }

                subject to grade
            }
        }
}