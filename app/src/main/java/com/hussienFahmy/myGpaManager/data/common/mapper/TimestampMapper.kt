package com.hussienfahmy.myGpaManager.data.common.mapper

import com.google.firebase.Timestamp
import com.hussienfahmy.core.domain.common.model.DomainTimestamp

/**
 * Mapper between Firebase Timestamp and domain-level DomainTimestamp
 */
fun Timestamp.toDomain(): DomainTimestamp {
    return DomainTimestamp(
        seconds = this.seconds,
        nanoseconds = this.nanoseconds
    )
}

fun DomainTimestamp.toFirebase(): Timestamp {
    return Timestamp(this.seconds, this.nanoseconds)
}