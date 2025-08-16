package com.hussienfahmy.gpa_system_settings_data.repository

import com.hussienfahmy.core.domain.gpa_settings.model.GPA
import com.hussienfahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienfahmy.gpa_system_settings_data.datastore.GPADatastore
import com.hussienfahmy.gpa_system_settings_data.mappers.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.hussienfahmy.gpa_system_settings_data.model.GPA as GPASystemData

internal class GPASettingsRepositoryImpl(
    private val dataStore: GPADatastore,
) : GPASettingsRepository {
    override fun observeGPA(): Flow<GPA> {
        return dataStore.observeGPASystem().map { it.toDomain() }
    }

    override suspend fun getGPASettings(): GPA {
        return dataStore.getGPASystem().toDomain()
    }

    override suspend fun updateGPASystem(system: GPA.System) {
        dataStore.updateGPASystem(
            when (system) {
                GPA.System.FOUR -> GPASystemData.System.FOUR
                GPA.System.FIVE -> GPASystemData.System.FIVE
            }
        )
    }
}