package com.hussienfahmy.subject_settings_presentation

import com.hussienfahmy.subject_settings_domain.model.SubjectSettings

sealed class SubjectSettingsEvent {
    data class UpdateSubjectMarksDependsOn(val dependsOn: SubjectSettings.SubjectsMarksDependsOn) :
        SubjectSettingsEvent()

    data class UpdateMarksPerCreditHour(val marksPerCreditHour: String) : SubjectSettingsEvent()
    data class UpdateConstantMarks(val constantMarks: String) : SubjectSettingsEvent()
}