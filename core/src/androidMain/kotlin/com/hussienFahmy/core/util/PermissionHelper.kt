package com.hussienfahmy.core.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

object PermissionHelper {

    private lateinit var launcher: ActivityResultLauncher<String>

    fun init(activity: ComponentActivity) {
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Notifications don't require permission below API 33
        }
    }

    @SuppressLint("InlinedApi")
    fun requestNotificationPermission(context: Context) {
        if (!hasNotificationPermission(context)) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}