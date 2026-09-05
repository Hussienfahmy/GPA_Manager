package com.hussienfahmy.myGpaManager.data.sync.model

import com.hussienfahmy.myGpaManager.data.common.mapper.LenientEpochMillisSerializer
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
    @Serializable(with = LenientEpochMillisSerializer::class)
    val lastUpdate: Long? = null,
)
