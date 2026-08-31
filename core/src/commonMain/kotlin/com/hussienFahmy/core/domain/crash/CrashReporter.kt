package com.hussienfahmy.core.domain.crash

interface CrashReporter {
    fun recordException(throwable: Throwable, metadata: Map<String, String> = emptyMap())
    fun log(message: String)
    fun setUserId(userId: String?)
}
