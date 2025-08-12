package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChangeProjectMarks @Inject constructor(
    private val subjectDao: SubjectDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult =
        withContext(backgroundDispatcher) {
            val projectMarks = marks.toDoubleOrNull()
            return@withContext if (projectMarks != null && projectMarks < 0) {
                UpdateResult.Failed(UiText.StringResource(R.string.err_subject_project_negative))
            } else {
                subjectDao.updateProject(subjectId, projectMarks)
                UpdateResult.Success
            }
        }
}