package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker
import com.hussienfahmy.core.model.UiText

class ChangeProjectMarks(
    private val subjectDao: SubjectDao,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke(subjectId: Long, marks: String): UpdateResult {
        val projectMarks = marks.toDoubleOrNull()
        return if (projectMarks != null && projectMarks < 0) {
            UpdateResult.Failed(UiText.Resource(Res.string.err_subject_project_negative))
        } else {
            subjectDao.updateProject(subjectId, projectMarks)
            dirtyTracker.markSubjectsChanged()
            UpdateResult.Success
        }
    }
}