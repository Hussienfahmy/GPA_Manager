package com.hussienfahmy.myGpaManager

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

// Called from iosApp/iosApp/iOSApp.swift (ComposeView.swift's UIViewControllerRepresentable) to
// get the root UIViewController hosting the whole Compose UI - the iOS analog of :app's
// MainActivity.setContent { GpaManagerApp() }.
fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        GpaManagerApp()
    }
}
