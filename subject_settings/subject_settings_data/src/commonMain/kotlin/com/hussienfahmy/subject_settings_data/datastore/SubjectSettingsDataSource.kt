package com.hussienfahmy.subject_settings_data.datastore

import com.hussienfahmy.core.data.local.datastore.createDataStore
import com.hussienfahmy.core.util.PlatformContext
import com.hussienfahmy.subject_settings_data.model.SubjectSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

internal class SubjectSettingsDataSource(
    context: PlatformContext,
    serializer: SubjectSettingsSerializer
) {
    private val dataSource = createDataStore(context, "subjects_settings", serializer)

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
