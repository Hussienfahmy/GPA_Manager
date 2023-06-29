package com.hussienfahmy.subject_settings_domain.repository

import com.hussienfahmy.subject_settings_domain.model.SubjectSettings
import kotlinx.coroutines.flow.Flow

interface SubjectSettingsRepository {

    fun observeSubjectSettings(): Flow<SubjectSettings>

    suspend fun getSubjectSettings(): SubjectSettings

    suspend fun updateSubjectsDependsOn(subjectsDependsOn: SubjectSettings.SubjectsMarksDependsOn)

    suspend fun updateConstantMarks(constantMarks: Double)

    suspend fun updateMarksPerCreditHour(marksPerCreditHour: Double)
}