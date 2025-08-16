package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.myGpaManager.core.R

class ChangeOralMarks(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult {
        val oral = marks.toDoubleOrNull()
        return if (oral != null && oral < 0) {
            UpdateResult.Failed(UiText.StringResource(R.string.err_subject_oral_negative))
        } else {
            subjectDao.updateOral(subjectId, oral)
            UpdateResult.Success
        }
    }
}