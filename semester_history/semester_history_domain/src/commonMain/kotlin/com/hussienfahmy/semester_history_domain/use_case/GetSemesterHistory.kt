package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetSemesterHistory(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
) {
    operator fun invoke(): Flow<List<Semester>> {
        return combine(
            semesterDao.getArchived(),
            subjectDao.getSemesterIdsWithMissingGrade(),
        ) { entities, missingGradeSemesterIds ->
            entities.map { entity ->
                entity.toDomain(hasMissingGrade = entity.id in missingGradeSemesterIds)
            }
        }
    }
}
