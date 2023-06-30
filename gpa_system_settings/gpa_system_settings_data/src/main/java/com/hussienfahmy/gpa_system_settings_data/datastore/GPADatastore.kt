package com.hussienfahmy.gpa_system_settings_data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.hussienfahmy.gpa_system_settings_data.model.GPA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

internal class GPADatastore(
    context: Context,
) {
    private val Context.dataStore by dataStore("gpa_system", GPASerializer)

    private val dataSource = context.dataStore

    fun observeGPASystem(): Flow<GPA> {
        return dataSource.data
    }

    suspend fun getGPASystem(): GPA {
        return dataSource.data.first()
    }

    suspend fun updateGPASystem(system: GPA.System) {
        dataSource.updateData { settings ->
            settings.copy(
                system = system
            )
        }
    }
}