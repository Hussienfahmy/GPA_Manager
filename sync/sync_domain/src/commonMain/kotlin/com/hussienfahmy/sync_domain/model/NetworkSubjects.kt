package com.hussienfahmy.sync_domain.model

import com.hussienfahmy.core.domain.common.model.DomainTimestamp

data class NetworkSubjects(
    val subjects: List<Subject> = emptyList(),
    val lastUpdate: DomainTimestamp? = null,
)