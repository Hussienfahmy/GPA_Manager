package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R

class ChangePracticalMarks(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult {
        val practical = marks.toDoubleOrNull()
        return if (practical != null && practical < 0) {
            UpdateResult.Failed(UiText.StringResource(R.string.err_subject_practical_negative))
        } else {
            subjectDao.updatePractical(subjectId, practical)
            UpdateResult.Success
        }
    }
}