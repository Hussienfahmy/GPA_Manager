package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao

class DeleteSubjectFromSemester(private val subjectDao: SubjectDao) {
    suspend operator fun invoke(subjectId: Long) {
        subjectDao.delete(subjectId)
    }
}
