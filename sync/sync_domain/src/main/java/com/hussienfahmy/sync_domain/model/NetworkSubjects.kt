package com.hussienfahmy.sync_domain.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class NetworkSubjects(
    val subjects: List<Subject> = emptyList(),
    val lastUpdate: Timestamp? = null,
)