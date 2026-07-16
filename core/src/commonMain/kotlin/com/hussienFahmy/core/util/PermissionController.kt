package com.hussienfahmy.core.util

enum class AppPermission {
    Notifications,
}

// iOS actual is deferred to the iOS phase, since nothing consumes it yet. Unlike the other :core
// expect/actuals so far, this one is NOT created via a GlobalContext-backed factory function - the
// Android actual must be constructed with the current Activity before it reaches STARTED (an
// androidx.activity.result.ActivityResultLauncher registration constraint), so callers construct
// it explicitly at the same point they used to call the old PermissionHelper.init(activity).
expect class PermissionController {
    fun hasPermission(permission: AppPermission): Boolean
    fun requestPermission(permission: AppPermission)
}
