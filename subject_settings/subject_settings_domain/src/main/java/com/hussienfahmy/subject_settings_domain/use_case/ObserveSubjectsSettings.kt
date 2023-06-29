package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienfahmy.subject_settings_domain.repository.SubjectSettingsRepository

class ObserveSubjectsSettings(
    private val repository: SubjectSettingsRepository
) {
    operator fun invoke() = repository.observeSubjectSettings()
}