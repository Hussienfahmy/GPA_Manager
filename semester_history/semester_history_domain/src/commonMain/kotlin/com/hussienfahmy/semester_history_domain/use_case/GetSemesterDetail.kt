package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.semester_history_domain.model.SemesterDetail
import com.hussienfahmy.semester_history_domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


class GetSemesterDetail(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
) {
    operator fun invoke(semesterId: Long): Flow<SemesterDetail?> {
        return combine(
            semesterDao.observeById(semesterId),
            subjectDao.getSubjectsBySemesterId(semesterId),
        ) { entity, subjects ->
            entity?.let {
                SemesterDetail(
                    semester = it.toDomain(),
                    subjects = subjects,
                )
            }
        }
    }
}
