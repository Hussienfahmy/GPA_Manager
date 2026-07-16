package com.hussienfahmy.myGpaManager.data.sync.model

import com.hussienfahmy.sync_domain.model.CalculationSettings
import com.hussienfahmy.sync_domain.model.NetworkGrade
import kotlinx.serialization.Serializable

/**
 * Firebase-specific data model for Settings
 * This isolates Firebase dependencies from domain models
 */
@Serializable
data class FirebaseSettings(
    val networkGrades: List<NetworkGrade> = listOf(),
    val calculationSettings: CalculationSettings = CalculationSettings(),
    val lastUpdate: Long? = null
)