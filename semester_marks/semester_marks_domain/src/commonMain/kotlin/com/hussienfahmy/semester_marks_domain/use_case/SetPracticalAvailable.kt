package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao

class SetPracticalAvailable(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long, isAvailable: Boolean) {
        subjectDao.setPracticalAvailability(subjectId, isAvailable)
    }
}