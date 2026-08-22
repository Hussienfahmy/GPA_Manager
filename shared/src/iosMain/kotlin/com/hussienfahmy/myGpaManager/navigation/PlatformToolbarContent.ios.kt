package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    // Root Scaffold already places this past the status bar, no extra inset needed here.
    UIKitView(
        modifier = Modifier.fillMaxWidth().height(44.dp).background(backgroundColor),
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
