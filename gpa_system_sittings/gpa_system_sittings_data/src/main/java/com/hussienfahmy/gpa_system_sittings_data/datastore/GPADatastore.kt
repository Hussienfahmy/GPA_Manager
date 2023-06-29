package com.hussienfahmy.gpa_system_sittings_data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.hussienfahmy.gpa_system_sittings_data.model.GPA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

// todo make all classes internal
class GPADatastore(
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