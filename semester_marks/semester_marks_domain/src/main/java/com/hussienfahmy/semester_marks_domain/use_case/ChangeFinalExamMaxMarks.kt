package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.model.UiText

class ChangeFinalExamMaxMarks(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult {
        val value = marks.toDoubleOrNull()
        return if (value == null || value <= 0) {
            UpdateResult.Failed(UiText.StringResource(R.string.err_subject_final_exam_invalid))
        } else {
            subjectDao.updateFinalExamMaxMarks(subjectId, value)
            UpdateResult.Success
        }
    }
}
