package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetProjectAvailable @Inject constructor(
    private val subjectDao: SubjectDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(subjectId: Long, isAvailable: Boolean) =
        withContext(backgroundDispatcher) {
            subjectDao.setProjectAvailability(subjectId, isAvailable)
        }
}