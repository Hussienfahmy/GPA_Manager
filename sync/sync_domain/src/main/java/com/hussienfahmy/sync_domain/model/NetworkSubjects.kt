package com.hussienfahmy.sync_domain.model

import androidx.annotation.Keep
import com.hussienfahmy.core.domain.common.model.DomainTimestamp

@Keep
data class NetworkSubjects(
    val subjects: List<Subject> = emptyList(),
    val lastUpdate: DomainTimestamp? = null,
)