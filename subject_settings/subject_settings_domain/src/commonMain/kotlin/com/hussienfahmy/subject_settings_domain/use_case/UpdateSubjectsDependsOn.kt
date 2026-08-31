package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker

class UpdateSubjectsDependsOn(
    private val repository: SubjectSettingsRepository,
    private val applySettingsToSubjects: ApplySettingsToSubjects,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke(subjectsDependsOn: SubjectSettings.SubjectsMarksDependsOn): UpdateResult {
        repository.updateSubjectsDependsOn(subjectsDependsOn)
        dirtyTracker.markSettingsChanged()
        applySettingsToSubjects()
        return UpdateResult.Success
    }
}