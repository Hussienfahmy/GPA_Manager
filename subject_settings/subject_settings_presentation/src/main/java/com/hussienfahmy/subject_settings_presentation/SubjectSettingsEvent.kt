package com.hussienfahmy.subject_settings_presentation

import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings

sealed class SubjectSettingsEvent {
    data class UpdateSubjectMarksDependsOn(val dependsOn: SubjectSettings.SubjectsMarksDependsOn) :
        SubjectSettingsEvent()

    data class UpdateMarksPerCreditHour(val marksPerCreditHour: String) : SubjectSettingsEvent()
    data class UpdateConstantMarks(val constantMarks: String) : SubjectSettingsEvent()
}