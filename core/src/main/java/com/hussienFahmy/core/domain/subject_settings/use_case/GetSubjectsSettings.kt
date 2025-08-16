package com.hussienfahmy.core.domain.subject_settings.use_case

import com.hussienfahmy.core.domain.subject_settings.repository.SubjectSettingsRepository

class GetSubjectsSettings(
    private val repository: SubjectSettingsRepository
) {
    suspend operator fun invoke() = repository.getSubjectSettings()
}