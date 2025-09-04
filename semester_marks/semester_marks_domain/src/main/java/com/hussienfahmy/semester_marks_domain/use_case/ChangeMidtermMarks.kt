package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.model.UiText

class ChangeMidtermMarks(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult = run {
        val midterm = marks.toDoubleOrNull()
        return@run if (midterm != null && midterm < 0) {
            UpdateResult.Failed(UiText.StringResource(R.string.err_subject_midterm_negative))
        } else {
            subjectDao.updateMidterm(subjectId, midterm)
            UpdateResult.Success
        }
    }
}