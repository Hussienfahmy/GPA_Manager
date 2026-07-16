package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.model.UiText

class ChangeFinalExamMaxMarks(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult {
        val value = marks.toDoubleOrNull()
        if (value == null || value <= 0) {
            return UpdateResult.Failed(UiText.Resource(Res.string.err_subject_final_exam_invalid))
        }

        val subject = subjectDao.getSubjectById(subjectId)
        if (subject != null) {
            // final exam total is a portion of the subject total marks
            if (value > subject.totalMarks) {
                return UpdateResult.Failed(
                    UiText.Resource(Res.string.err_subject_final_exam_exceeds_total)
                )
            }

            // semester work marks the student has already entered (their scores).
            // a final-exam total smaller than the coursework scores means the
            // student typed what they scored in the exam, not what it is out of.
            val semesterMarks = subject.semesterMarks
            val semesterWork = (semesterMarks?.midterm ?: 0.0) +
                    (semesterMarks?.practical ?: 0.0) +
                    (semesterMarks?.oral ?: 0.0) +
                    (semesterMarks?.project ?: 0.0)
            if (semesterWork > 0 && value <= semesterWork) {
                return UpdateResult.Failed(
                    UiText.Resource(Res.string.err_subject_final_exam_below_semester)
                )
            }
        }

        subjectDao.updateFinalExamMaxMarks(subjectId, value)
        return UpdateResult.Success
    }
}
