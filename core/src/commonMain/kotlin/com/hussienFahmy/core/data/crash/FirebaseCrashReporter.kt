package com.hussienFahmy.core.data.crash

import com.hussienfahmy.core.domain.crash.CrashReporter
import dev.gitlive.firebase.crashlytics.FirebaseCrashlytics

class FirebaseCrashReporter(
    private val crashlytics: FirebaseCrashlytics
) : CrashReporter {

    override fun recordException(throwable: Throwable, metadata: Map<String, String>) {
        metadata.forEach { (key, value) -> crashlytics.setCustomKey(key, value) }
        crashlytics.recordException(throwable)
    }

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun setUserId(userId: String?) {
        // Same null-is-no-op convention as FirebaseAnalyticsService.setUserProperty - gitlive's
        // setUserId takes a non-nullable value, no API to clear it.
        if (userId != null) {
            crashlytics.setUserId(userId)
        }
    }
}
