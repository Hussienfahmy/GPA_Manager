package com.hussienfahmy.subject_settings_data.mappers

import com.hussienfahmy.subject_settings_data.model.SubjectSettings
import com.hussienFahmy.core.domain.subject_settings.model.SubjectSettings as DomainSubjectSettings


internal fun SubjectSettings.toDomain() = DomainSubjectSettings(
    subjectsMarksDependsOn = when (subjectsMarksDependsOn) {
        SubjectSettings.SubjectsMarksDependsOn.CREDIT -> DomainSubjectSettings.SubjectsMarksDependsOn.CREDIT
        SubjectSettings.SubjectsMarksDependsOn.CONSTANT -> DomainSubjectSettings.SubjectsMarksDependsOn.CONSTANT
    },
    constantMarks = constantMarks,
    marksPerCreditHour = marksPerCreditHour,
)

internal fun DomainSubjectSettings.toData() = SubjectSettings(
    subjectsMarksDependsOn = when (subjectsMarksDependsOn) {
        DomainSubjectSettings.SubjectsMarksDependsOn.CREDIT -> SubjectSettings.SubjectsMarksDependsOn.CREDIT
        DomainSubjectSettings.SubjectsMarksDependsOn.CONSTANT -> SubjectSettings.SubjectsMarksDependsOn.CONSTANT
    },
    constantMarks = constantMarks,
    marksPerCreditHour = marksPerCreditHour,
)