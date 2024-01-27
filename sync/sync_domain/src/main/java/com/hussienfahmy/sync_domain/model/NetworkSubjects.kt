package com.hussienfahmy.sync_domain.model

import androidx.annotation.Keep

@Keep
data class NetworkSubjects(
    val subjects: List<Subject> = emptyList()
)