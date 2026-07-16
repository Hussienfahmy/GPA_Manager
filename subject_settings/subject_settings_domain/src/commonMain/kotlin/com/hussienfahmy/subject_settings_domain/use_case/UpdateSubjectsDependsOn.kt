package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core.domain.subject_settings.repository.SubjectSettingsRepository

class UpdateSubjectsDependsOn(
    private val repository: SubjectSettingsRepository,
    private val applySettingsToSubjects: ApplySettingsToSubjects
) {
    suspend operator fun invoke(subjectsDependsOn: SubjectSettings.SubjectsMarksDependsOn): UpdateResult {
        repository.updateSubjectsDependsOn(subjectsDependsOn)
        applySettingsToSubjects()
        return UpdateResult.Success
    }
}