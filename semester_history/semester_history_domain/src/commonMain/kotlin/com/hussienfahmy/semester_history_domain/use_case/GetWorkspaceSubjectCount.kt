package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import kotlinx.coroutines.flow.Flow

class GetWorkspaceSubjectCount(private val subjectDao: SubjectDao) {
    operator fun invoke(): Flow<Int> = subjectDao.getWorkspaceSubjectCount()
}
