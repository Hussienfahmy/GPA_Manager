package com.hussienfahmy.core.domain.common.model

/**
 * Domain-level timestamp abstraction that's independent of any specific backend implementation.
 *
 * `now()` and `toDate()` still use JVM-only APIs (`java.lang.System`, `java.util.Date`) - this
 * compiles today because :core only targets androidTarget, but both need replacing with
 * kotlinx-datetime (or an expect/actual, pending confirmation) before an iosMain source set is
 * added to this module.
 */
data class DomainTimestamp(
    val seconds: Long,
    val nanoseconds: Int = 0
) {
    companion object {
        fun now(): DomainTimestamp {
            val now = System.currentTimeMillis()
            return DomainTimestamp(
                seconds = now / 1000,
                nanoseconds = ((now % 1000) * 1_000_000).toInt()
            )
        }
    }

    fun toMillis(): Long = seconds * 1000 + nanoseconds / 1_000_000

    fun toDate(): java.util.Date = java.util.Date(toMillis())
}