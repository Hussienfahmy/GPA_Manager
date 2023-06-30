package com.hussienfahmy.gpa_system_sittings_domain.repository

import com.hussienfahmy.gpa_system_sittings_domain.model.GPA
import kotlinx.coroutines.flow.Flow

interface GPASystemRepository {

    fun observeGPA(): Flow<GPA>

    suspend fun getGPA(): GPA

    suspend fun updateGPASystem(system: GPA.System)
}