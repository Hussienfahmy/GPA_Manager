package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienFahmy.core.domain.subject_settings.use_case.GetSubjectsSettings

data class SubjectSettingsUseCases(
    val observeSubjectSettings: ObserveSubjectsSettings,
    val getSubjectSettings: GetSubjectsSettings,
    val updateSubjectsDependsOn: UpdateSubjectsDependsOn,
    val updateConstantMarks: UpdateConstantMarks,
    val updateMarksPerCreditHour: UpdateMarksPerCreditHours
)
