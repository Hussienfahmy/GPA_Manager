package com.hussienfahmy.myGpaManager

import androidx.compose.ui.window.ComposeUIViewController
import com.hussienfahmy.core.util.AppPermission
import com.hussienfahmy.core.util.PermissionController
import platform.UIKit.UIViewController

// Called from iosApp/iosApp/iOSApp.swift (ComposeView.swift's UIViewControllerRepresentable) to
// get the root UIViewController hosting the whole Compose UI - the iOS analog of :app's
// MainActivity.setContent { GpaManagerApp(...) }. Best-effort, unverified - no simulator/device
// available in this sandbox.
fun MainViewController(): UIViewController {
    val permissionController = PermissionController()
    return ComposeUIViewController {
        GpaManagerApp(
            onRequestNotificationPermission = {
                permissionController.requestPermission(AppPermission.Notifications)
            },
        )
    }
}
