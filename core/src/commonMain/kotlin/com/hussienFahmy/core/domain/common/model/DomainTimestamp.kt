package com.hussienfahmy.core.domain.common.model

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Domain-level timestamp abstraction that's independent of any specific backend implementation.
 */
data class DomainTimestamp(
    val seconds: Long,
    val nanoseconds: Int = 0
) {
    companion object {
        @OptIn(ExperimentalTime::class)
        fun now(): DomainTimestamp {
            val now = Clock.System.now()
            return DomainTimestamp(
                seconds = now.epochSeconds,
                nanoseconds = now.nanosecondsOfSecond
            )
        }
    }

    fun toMillis(): Long = seconds * 1000 + nanoseconds / 1_000_000
}