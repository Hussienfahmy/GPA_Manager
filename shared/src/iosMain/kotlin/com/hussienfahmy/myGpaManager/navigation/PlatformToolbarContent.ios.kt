package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.cstr
import platform.Foundation.NSSelectorFromString
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIBarButtonItemStyle
import platform.UIKit.UIImage
import platform.UIKit.UIImageRenderingMode
import platform.UIKit.UINavigationBar
import platform.UIKit.UINavigationBarAppearance
import platform.UIKit.UINavigationItem
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN_NONATOMIC
import platform.objc.objc_setAssociatedObject

@OptIn(ExperimentalComposeUiApi::class, ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun PlatformToolbarContent(
    title: String?,
    accentColor: Color,
    backgroundColor: Color,
    onBackClick: () -> Unit,
) {
    // UIKitView bridges a raw UINavigationBar outside any real UINavigationController, so it gets
    // no safe-area propagation of its own - the status bar inset has to be reserved explicitly
    // here, backed by the same color, so the native bar and the reserved strip above it read as
    // one continuous surface instead of the bar rendering under the status bar/notch.
    Column(modifier = Modifier.fillMaxWidth().background(backgroundColor)) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        UIKitView(
            modifier = Modifier.fillMaxWidth().height(44.dp),
            properties = UIKitInteropProperties(
                interactionMode = UIKitInteropInteractionMode.NonCooperative,
                placedAsOverlay = true,
            ),
            factory = {
                val navBar = UINavigationBar()
                applyToolbarAppearance(navBar, accentColor)

                val backIcon = UIImage.systemImageNamed("chevron.backward")
                    ?.imageWithRenderingMode(UIImageRenderingMode.UIImageRenderingModeAlwaysTemplate)
                val target = BackButtonTarget(onBackClick)
                objc_setAssociatedObject(
                    `object` = navBar,
                    key = "backButtonTarget".cstr,
                    value = target,
                    policy = OBJC_ASSOCIATION_RETAIN_NONATOMIC,
                )
                val backButton = UIBarButtonItem(
                    image = backIcon,
                    style = UIBarButtonItemStyle.UIBarButtonItemStylePlain,
                    target = target,
                    action = NSSelectorFromString(target::onBackTapped.name),
                )

                val navItem = UINavigationItem(title = title ?: "")
                navItem.leftBarButtonItem = backButton
                navBar.setItems(listOf(navItem), animated = false)

                navBar
            },
            update = { navBar ->
                applyToolbarAppearance(navBar, accentColor)
                navBar.topItem?.title = title ?: ""
            },
        )
    }
}

private fun applyToolbarAppearance(navBar: UINavigationBar, accentColor: Color) {
    val appearance = UINavigationBarAppearance().apply {
        configureWithDefaultBackground()
        titleTextAttributes = mapOf(NSForegroundColorAttributeName to accentColor.toUIColor())
    }
    navBar.standardAppearance = appearance
    navBar.scrollEdgeAppearance = appearance
    navBar.tintColor = accentColor.toUIColor()
}

@OptIn(BetaInteropApi::class)
private class BackButtonTarget(
    private val onBackClick: () -> Unit,
) : NSObject() {
    @ObjCAction
    fun onBackTapped() {
        onBackClick()
    }
}
