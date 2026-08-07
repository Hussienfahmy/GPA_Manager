package com.hussienfahmy.core.util

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

actual class PermissionController {
    actual fun hasPermission(permission: AppPermission): Boolean {
        // UNUserNotificationCenter's authorization status is only obtainable via an async
        // callback (getNotificationSettingsWithCompletionHandler); there's no synchronous check.
        // Returns false conservatively - callers only use this to skip an already-granted
        // permission before calling requestPermission() below, which is a real, working
        // implementation - so a false negative here just means one extra (harmless, system-deduped)
        // permission prompt, not a functional gap.
        return false
    }

    actual suspend fun requestPermission(permission: AppPermission) {
        when (permission) {
            AppPermission.Notifications -> requestNotificationAuthorization()
        }
    }

    private suspend fun requestNotificationAuthorization(): Unit =
        suspendCancellableCoroutine { continuation ->
            val options = UNAuthorizationOptionAlert or
                UNAuthorizationOptionSound or
                UNAuthorizationOptionBadge
            UNUserNotificationCenter.currentNotificationCenter()
                .requestAuthorizationWithOptions(options) { _, _ ->
                    if (continuation.isActive) continuation.resume(Unit)
                }
        }
}
