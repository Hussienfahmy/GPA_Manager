package com.hussienfahmy.core.data.analytics

import com.hussienfahmy.core.domain.analytics.AnalyticsService
import dev.gitlive.firebase.analytics.FirebaseAnalytics

// GitLive's logEvent(name, parameters: Map<String, Any>?) takes a plain Kotlin map directly - the
// Android Bundle-building logic this class previously needed is gone entirely, not just ported.
class FirebaseAnalyticsService(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsService {

    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        firebaseAnalytics.logEvent(eventName, parameters)
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun setUserProperty(propertyName: String, propertyValue: String?) {
        // GitLive's setUserProperty takes a non-nullable value (unlike the Android SDK, which
        // allows null to clear a property) - null here is a no-op rather than clearing the
        // property. No caller in this codebase currently passes null, but flagging the behavior
        // difference in case that changes.
        if (propertyValue != null) {
            firebaseAnalytics.setUserProperty(propertyName, propertyValue)
        }
    }
}
