package com.hussienfahmy.subject_settings_data.repository

import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienfahmy.subject_settings_data.datastore.SubjectSettingsDataSource
import com.hussienfahmy.subject_settings_data.mappers.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.hussienfahmy.subject_settings_data.model.SubjectSettings as SubjectSettingsData

internal class SubjectSettingsRepositoryImpl(
    private val subjectSettingsDataSource: SubjectSettingsDataSource
) : SubjectSettingsRepository {

    override fun observeSubjectSettings(): Flow<SubjectSettings> {
        return subjectSettingsDataSource.observeSubjectsSettings().map {
            it.toDomain()
        }
    }

    override suspend fun getSubjectSettings(): SubjectSettings {
        return subjectSettingsDataSource.getSubjectsSettings().toDomain()
    }

    override suspend fun updateSubjectsDependsOn(subjectsDependsOn: SubjectSettings.SubjectsMarksDependsOn) {
        subjectSettingsDataSource.updateSubjectsDependsOn(
            when (subjectsDependsOn) {
                SubjectSettings.SubjectsMarksDependsOn.CREDIT -> SubjectSettingsData.SubjectsMarksDependsOn.CREDIT
                SubjectSettings.SubjectsMarksDependsOn.CONSTANT -> SubjectSettingsData.SubjectsMarksDependsOn.CONSTANT
            }
        )
    }

    override suspend fun updateConstantMarks(constantMarks: Double) {
        subjectSettingsDataSource.updateConstantMarks(constantMarks)
    }

    override suspend fun updateMarksPerCreditHour(marksPerCreditHour: Double) {
        subjectSettingsDataSource.updateMarksPerCreditHour(marksPerCreditHour)
    }
}