package com.hussienFahmy.core.domain.subject_settings.repository

import com.hussienFahmy.core.domain.subject_settings.model.SubjectSettings
import kotlinx.coroutines.flow.Flow

interface SubjectSettingsRepository {

    fun observeSubjectSettings(): Flow<SubjectSettings>

    suspend fun getSubjectSettings(): SubjectSettings

    suspend fun updateSubjectsDependsOn(subjectsDependsOn: SubjectSettings.SubjectsMarksDependsOn)

    suspend fun updateConstantMarks(constantMarks: Double)

    suspend fun updateMarksPerCreditHour(marksPerCreditHour: Double)
}