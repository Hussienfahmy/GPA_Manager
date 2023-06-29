package com.hussienfahmy.subject_settings_data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.hussienfahmy.subject_settings_data.model.SubjectSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

// todo make all internal
class SubjectSettingsDataSource(
    context: Context,
) {
    private val Context.dataStore by dataStore("subjects_settings", SubjectSettingsSerializer)

    private val dataSource = context.dataStore

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
