package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ChangeMidtermMarks(
    private val subjectDao: SubjectDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult =
        withContext(backgroundDispatcher) {
            val midterm = marks.toDoubleOrNull()
            return@withContext if (midterm != null && midterm < 0) {
                UpdateResult.Failed(UiText.StringResource(R.string.err_subject_midterm_negative))
            } else {
                subjectDao.updateMidterm(subjectId, midterm)
                UpdateResult.Success
            }
        }
}