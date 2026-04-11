package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSemesterHistory(
    private val semesterDao: SemesterDao,
) {
    operator fun invoke(): Flow<List<Semester>> {
        return semesterDao.getArchived().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
