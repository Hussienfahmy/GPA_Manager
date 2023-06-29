package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.subject_settings_domain.model.SubjectSettings
import com.hussienfahmy.subject_settings_domain.repository.SubjectSettingsRepository

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