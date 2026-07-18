package com.hussienfahmy.core.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

actual class PermissionController(private val activity: ComponentActivity) {

    private val notificationPermissionLauncher: ActivityResultLauncher<String> =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    actual fun hasPermission(permission: AppPermission): Boolean = when (permission) {
        AppPermission.Notifications -> hasNotificationPermission()
    }

    @SuppressLint("InlinedApi")
    actual suspend fun requestPermission(permission: AppPermission) {
        when (permission) {
            AppPermission.Notifications -> {
                if (!hasNotificationPermission()) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Notifications don't require permission below API 33
        }
    }
}
