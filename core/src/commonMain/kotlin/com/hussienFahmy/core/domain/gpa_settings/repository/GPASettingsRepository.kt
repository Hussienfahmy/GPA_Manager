package com.hussienfahmy.core.domain.gpa_settings.repository

import com.hussienfahmy.core.domain.gpa_settings.model.GPA
import kotlinx.coroutines.flow.Flow

interface GPASettingsRepository {

    fun observeGPA(): Flow<GPA>

    suspend fun getGPASettings(): GPA

    suspend fun updateGPASystem(system: GPA.System)
}