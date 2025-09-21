package com.hussienfahmy.myGpaManager.data.sync.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.hussienfahmy.sync_domain.model.Subject

/**
 * Firebase-specific data model for NetworkSubjects
 * This isolates Firebase dependencies from domain models
 */
@Keep
data class FirebaseNetworkSubjects(
    val subjects: List<Subject> = emptyList(),
    val lastUpdate: Timestamp? = null,
)