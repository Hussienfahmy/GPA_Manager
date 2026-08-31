package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core.domain.subject_settings.use_case.GetSubjectsSettings
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker

class ApplySettingsToSubjects(
    private val subjectDao: SubjectDao,
    private val getSubjectsSettings: GetSubjectsSettings,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke() {
        val settings = getSubjectsSettings()
        when (settings.subjectsMarksDependsOn) {
            SubjectSettings.SubjectsMarksDependsOn.CREDIT -> subjectDao.updateTotalMarksPerCredit(
                settings.marksPerCreditHour
            )

            SubjectSettings.SubjectsMarksDependsOn.CONSTANT -> subjectDao.updateTotalMarksConst(
                settings.constantMarks
            )
        }
        dirtyTracker.markSubjectsChanged()
    }
}