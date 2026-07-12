package com.hussienfahmy.subject_settings_data.datastore

import com.hussienfahmy.core.data.local.datastore.createDataStore
import com.hussienfahmy.subject_settings_data.model.SubjectSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SubjectSettingsDataSource {
    private val dataSource = createDataStore("subjects_settings", SubjectSettingsSerializer)

    fun observeSubjectsSettings(): Flow<SubjectSettings> {
        return dataSource.data
    }

    suspend fun getSubjectsSettings(): SubjectSettings {
        return dataSource.data.first()
    }

    suspend fun updateSubjectsDependsOn(subjectsDependsOn: SubjectSettings.SubjectsMarksDependsOn) {
        dataSource.updateData { settings ->
            settings.copy(
                subjectsMarksDependsOn = subjectsDependsOn
            )
        }
    }

    suspend fun updateConstantMarks(constantMarks: Double) {
        dataSource.updateData { settings ->
            settings.copy(
                constantMarks = constantMarks
            )
        }
    }

    suspend fun updateMarksPerCreditHour(marksPerCreditHour: Double) {
        dataSource.updateData { settings ->
            settings.copy(
                marksPerCreditHour = marksPerCreditHour
            )
        }
    }
}
