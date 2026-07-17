package com.hussienfahmy.core.util

// STUB: iOS's real notification-permission API (UNUserNotificationCenter) is callback/async-only
// (getNotificationSettingsWithCompletionHandler / requestAuthorizationWithOptions), but this
// class's commonMain signature is synchronous (hasPermission(): Boolean, requestPermission():
// Unit) - a holdover from Android's synchronous ActivityCompat.checkSelfPermission API. A
// faithful iOS implementation isn't possible without either blocking on the async callback (risky
// on Kotlin/Native's memory model) or changing the commonMain interface to be suspend-based. Left
// as a no-op stub rather than guessing at a synchronization strategy blind; revisit the interface
// shape before implementing this for real.
actual class PermissionController {
    actual fun hasPermission(permission: AppPermission): Boolean = false

    actual fun requestPermission(permission: AppPermission) {
        // TODO: implement via UNUserNotificationCenter once PermissionController's commonMain
        // interface is revisited to be suspend-based (or otherwise reactive).
    }
}
