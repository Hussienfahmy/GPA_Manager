package com.hussienfahmy.gpa_system_sittings_data.repository

import com.hussienfahmy.gpa_system_sittings_data.datastore.GPADatastore
import com.hussienfahmy.gpa_system_sittings_data.mappers.toDomain
import com.hussienfahmy.gpa_system_sittings_domain.model.GPA
import com.hussienfahmy.gpa_system_sittings_domain.repository.GPASystemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.hussienfahmy.gpa_system_sittings_data.model.GPA as GPASystemData

class GPASystemRepositoryImpl(
    private val dataStore: GPADatastore,
) : GPASystemRepository {
    override fun observeGPA(): Flow<GPA> {
        return dataStore.observeGPASystem().map { it.toDomain() }
    }

    override suspend fun getGPA(): GPA {
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