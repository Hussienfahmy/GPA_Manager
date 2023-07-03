package com.hussienfahmy.subject_settings_presentation

import com.hussienFahmy.core.domain.subject_settings.model.SubjectSettings

sealed class SubjectSettingsState {
    object Loading : SubjectSettingsState()
    data class Success(val subjectSettings: SubjectSettings) : SubjectSettingsState()
}