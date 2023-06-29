package com.hussienfahmy.subject_settings_presentation

import com.hussienfahmy.subject_settings_domain.model.SubjectSettings

sealed class SubjectSettingsState {
    object Loading : SubjectSettingsState()
    data class Success(val subjectSettings: SubjectSettings) : SubjectSettingsState()
}