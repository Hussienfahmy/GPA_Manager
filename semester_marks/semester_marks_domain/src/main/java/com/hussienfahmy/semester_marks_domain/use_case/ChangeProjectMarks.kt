package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.myGpaManager.core.R

class ChangeProjectMarks(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult {
        val projectMarks = marks.toDoubleOrNull()
        return if (projectMarks != null && projectMarks < 0) {
            UpdateResult.Failed(UiText.StringResource(R.string.err_subject_project_negative))
        } else {
            subjectDao.updateProject(subjectId, projectMarks)
            UpdateResult.Success
        }
    }
}