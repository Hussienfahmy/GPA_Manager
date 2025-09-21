package com.hussienfahmy.myGpaManager.data.sync.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.hussienfahmy.sync_domain.model.CalculationSettings
import com.hussienfahmy.sync_domain.model.NetworkGrade

/**
 * Firebase-specific data model for Settings
 * This isolates Firebase dependencies from domain models
 */
@Keep
data class FirebaseSettings(
    val networkGrades: List<NetworkGrade> = listOf(),
    val calculationSettings: CalculationSettings = CalculationSettings(),
    val lastUpdate: Timestamp? = null
)