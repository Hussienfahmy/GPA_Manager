package com.hussienfahmy.gpa_system_settings_data.datastore

import com.hussienfahmy.core.data.local.datastore.createDataStore
import com.hussienfahmy.core.util.PlatformContext
import com.hussienfahmy.gpa_system_settings_data.model.GPA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

internal class GPADatastore(context: PlatformContext, serializer: GPASerializer) {
    private val dataSource = createDataStore(context, "gpa_system", serializer)

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
