package com.hussienfahmy.core.data.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.hussienfahmy.core.domain.analytics.AnalyticsService

class FirebaseAnalyticsService(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsService {

    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        val bundle = parameters?.let { convertToBundle(it) }
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun setUserProperty(propertyName: String, propertyValue: String?) {
        firebaseAnalytics.setUserProperty(propertyName, propertyValue)
    }

    private fun convertToBundle(parameters: Map<String, Any>): Bundle {
        val bundle = Bundle()
        parameters.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Float -> bundle.putFloat(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                else -> bundle.putString(key, value.toString())
            }
        }
        return bundle
    }
}