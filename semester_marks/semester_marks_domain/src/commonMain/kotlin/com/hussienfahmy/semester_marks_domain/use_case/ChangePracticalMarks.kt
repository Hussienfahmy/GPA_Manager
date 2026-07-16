package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.model.UiText

class ChangePracticalMarks(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult {
        val practical = marks.toDoubleOrNull()
        return if (practical != null && practical < 0) {
            UpdateResult.Failed(UiText.Resource(Res.string.err_subject_practical_negative))
        } else {
            subjectDao.updatePractical(subjectId, practical)
            UpdateResult.Success
        }
    }
}