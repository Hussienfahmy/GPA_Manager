package com.hussienfahmy.subject_settings_domain.use_case

data class SubjectSettingsUseCases(
    val observeSubjectSettings: ObserveSubjectsSettings,
    val getSubjectSettings: GetSubjectsSettings,
    val updateSubjectsDependsOn: UpdateSubjectsDependsOn,
    val updateConstantMarks: UpdateConstantMarks,
    val updateMarksPerCreditHour: UpdateMarksPerCreditHours
)
