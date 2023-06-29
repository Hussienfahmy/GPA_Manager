package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienfahmy.subject_settings_domain.repository.SubjectSettingsRepository

class GetSubjectsSettings(
    private val repository: SubjectSettingsRepository
) {
    suspend operator fun invoke() = repository.getSubjectSettings()
}