package com.hussienfahmy.core.domain.analytics

interface AnalyticsService {
    fun logEvent(eventName: String, parameters: Map<String, Any>? = null)
    fun setUserId(userId: String?)
    fun setUserProperty(propertyName: String, propertyValue: String?)
}