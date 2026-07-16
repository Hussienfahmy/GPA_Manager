package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao

class SetMidtermAvailable(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long, isAvailable: Boolean) {
        subjectDao.setMidtermAvailability(subjectId, isAvailable)
    }
}