package com.hussienfahmy.myGpaManager.data.sync.model

import com.hussienfahmy.myGpaManager.data.common.mapper.LenientEpochMillisSerializer
import com.hussienfahmy.sync_domain.model.Subject
import kotlinx.serialization.Serializable

/**
 * Firebase-specific data model for NetworkSubjects
 * This isolates Firebase dependencies from domain models
 */
@Serializable
data class FirebaseNetworkSubjects(
    val subjects: List<Subject> = emptyList(),
    @Serializable(with = LenientEpochMillisSerializer::class)
    val lastUpdate: Long? = null,
)
