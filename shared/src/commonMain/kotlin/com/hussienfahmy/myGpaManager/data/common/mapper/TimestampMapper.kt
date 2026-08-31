package com.hussienfahmy.myGpaManager.data.common.mapper

import com.hussienfahmy.core.domain.common.model.DomainTimestamp
import kotlin.time.Instant

/**
 * Mapper between an epoch-millis Long (how timestamps are now stored in Firestore documents,
 * client-computed rather than relying on GitLive's Firestore Timestamp/ServerTimestamp type,
 * which has known rough edges - see GitLiveApp/firebase-kotlin-sdk#666) and DomainTimestamp.
 */
fun Long.toDomainTimestamp(): DomainTimestamp {
    val instant = Instant.fromEpochMilliseconds(this)
    return DomainTimestamp(
        seconds = instant.epochSeconds,
        nanoseconds = instant.nanosecondsOfSecond
    )
}

fun DomainTimestamp.toEpochMillis(): Long {
    return Instant.fromEpochSeconds(seconds, nanoseconds).toEpochMilliseconds()
}
