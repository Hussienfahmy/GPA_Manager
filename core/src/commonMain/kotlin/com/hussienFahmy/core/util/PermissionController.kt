package com.hussienfahmy.core.util

enum class AppPermission {
    Notifications,
}

// Not created via a GlobalContext-backed factory function like most other :core expect/actuals -
// the Android actual must be constructed with the current Activity before it reaches STARTED (an
// androidx.activity.result.ActivityResultLauncher registration constraint), so callers construct
// it explicitly at the same point they used to call the old PermissionHelper.init(activity). The
// iOS actual has no such constraint and takes a no-arg constructor instead.
//
// requestPermission() is suspend (unlike the original Android-only design) because iOS's
// UNUserNotificationCenter authorization API is callback-based, not synchronous like Android's
// ActivityResultLauncher.launch(). The Android actual doesn't need to actually suspend - it still
// just fires the launcher and returns - so this is a source-compatible widening, not a behavior
// change on Android.
expect class PermissionController {
    fun hasPermission(permission: AppPermission): Boolean
    suspend fun requestPermission(permission: AppPermission)
}
