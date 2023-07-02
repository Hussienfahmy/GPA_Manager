package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SetMidtermAvailable(
    private val subjectDao: SubjectDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(subjectId: Long, isAvailable: Boolean) {
        withContext(backgroundDispatcher) {
            subjectDao.setMidtermAvailability(subjectId, isAvailable)
        }
    }
}