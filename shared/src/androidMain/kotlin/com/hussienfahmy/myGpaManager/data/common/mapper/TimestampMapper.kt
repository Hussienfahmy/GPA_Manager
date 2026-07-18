package com.hussienfahmy.myGpaManager.data.common.mapper

import com.hussienfahmy.core.domain.common.model.DomainTimestamp

/**
 * Mapper between an epoch-millis Long (how timestamps are now stored in Firestore documents,
 * client-computed rather than relying on GitLive's Firestore Timestamp/ServerTimestamp type,
 * which has known rough edges - see GitLiveApp/firebase-kotlin-sdk#666) and DomainTimestamp.
 */
fun Long.toDomainTimestamp(): DomainTimestamp {
    return DomainTimestamp(
        seconds = this / 1000,
        nanoseconds = ((this % 1000) * 1_000_000).toInt()
    )
}

fun DomainTimestamp.toEpochMillis(): Long {
    return seconds * 1000 + nanoseconds / 1_000_000
}
