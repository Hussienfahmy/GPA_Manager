package com.hussienfahmy.myGpaManager.navigation

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
import com.hussienfahmy.core_ui.theme.MeadowColors
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cstr
import org.jetbrains.compose.resources.stringResource
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.UIBlurEffect
import platform.UIKit.UIBlurEffectStyle
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.UIKit.UIImageRenderingMode
import platform.UIKit.UITabBar
import platform.UIKit.UITabBarAppearance
import platform.UIKit.UITabBarDelegateProtocol
import platform.UIKit.UITabBarItem
import platform.UIKit.UITabBarItemPositioning
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN_NONATOMIC
import platform.objc.objc_setAssociatedObject

// A real UITabBar embedded via UIKitView, not Compose-drawn - UIBlurEffect/UITabBarAppearance
// have been available since iOS 13, well under this app's 15.0 deployment target, so no fallback
// or OS-version check is needed here (unlike UIGlassEffect, which is iOS 26+ only and isn't used
// here). The one real trade-off: UITabBar only supports one global tint for all selected items,
// not the distinct per-tab accent color + pill highlight Android's Material3 bar has - see
// tabBarColor below.
@OptIn(ExperimentalComposeUiApi::class, ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun PlatformBottomNavContent(
    selectedRoute: AppRoute,
    colors: MeadowColors,
    onSelect: (BottomNavDestination) -> Unit,
) {
    val destinations = BottomNavDestination.entries
    // stringResource() needs a @Composable context, so labels are resolved here rather than
    // inside UIKitView's factory/update lambdas (which aren't composable scopes).
    val labels = destinations.map { stringResource(it.label) }
    val selectedIndex = destinations.indexOfFirst { it.route == selectedRoute }.coerceAtLeast(0)
    // UITabBar only supports one tint for every selected item, not a distinct color per tab -
    // colors.semester is this app's own default/primary accent (see MeadowTheme's
    // LocalMeadowAccent default), used here as that single tint.
    val tabBarColor = colors.semester.deep

    UIKitView(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative,
            placedAsOverlay = true,
        ),
        factory = {
            // itemPositioning belongs to UITabBar itself, not UITabBarAppearance.
            val tabBar = UITabBar().apply {
                itemPositioning = UITabBarItemPositioning.UITabBarItemPositioningCentered
            }

            val appearance = UITabBarAppearance().apply {
                configureWithTransparentBackground()
                backgroundEffect = UIBlurEffect.effectWithStyle(
                    UIBlurEffectStyle.UIBlurEffectStyleSystemUltraThinMaterial
                )
                backgroundColor = colors.navBg.toUIColor().colorWithAlphaComponent(0.72)
                shadowColor = UIColor.clearColor
                stackedLayoutAppearance.selected.iconColor = tabBarColor.toUIColor()
                stackedLayoutAppearance.selected.titleTextAttributes = mapOf(
                    NSForegroundColorAttributeName to tabBarColor.toUIColor()
                )
                stackedLayoutAppearance.normal.iconColor = colors.navItemIcon.toUIColor()
            }
            tabBar.standardAppearance = appearance
            tabBar.scrollEdgeAppearance = appearance

            val items = destinations.mapIndexed { index, destination ->
                val image = UIImage.systemImageNamed(destination.sfSymbol)
                    ?.imageWithRenderingMode(UIImageRenderingMode.UIImageRenderingModeAlwaysTemplate)
                UITabBarItem(title = labels[index], image = image, selectedImage = image).apply {
                    tag = index.toLong()
                }
            }
            tabBar.setItems(items, animated = false)
            tabBar.selectedItem = items.getOrNull(selectedIndex)

            // The delegate must outlive this factory call (UITabBar.delegate is a weak
            // reference), so it's retained on the tab bar itself via an associated object -
            // the standard ObjC idiom for this, same as AppleSignIn.kt's ASAuthorizationController
            // delegate elsewhere in this codebase.
            val delegate = TabBarDelegate { index ->
                destinations.getOrNull(index)?.let(onSelect)
            }
            objc_setAssociatedObject(
                `object` = tabBar,
                key = "tabBarDelegate".cstr,
                value = delegate,
                policy = OBJC_ASSOCIATION_RETAIN_NONATOMIC,
            )
            tabBar.delegate = delegate

            tabBar
        },
        update = { tabBar ->
            val items = tabBar.items ?: return@UIKitView
            val current = items.getOrNull(selectedIndex) as? UITabBarItem
            if (tabBar.selectedItem != current) {
                tabBar.selectedItem = current
            }
        },
    )
}

private val BottomNavDestination.sfSymbol: String
    get() = when (this) {
        BottomNavDestination.Semester -> "calendar"
        BottomNavDestination.Marks -> "checkmark"
        BottomNavDestination.History -> "clock.arrow.circlepath"
        BottomNavDestination.Quick -> "sparkles"
        BottomNavDestination.More -> "ellipsis"
    }

private fun Color.toUIColor(): UIColor =
    UIColor(red = red.toDouble(), green = green.toDouble(), blue = blue.toDouble(), alpha = alpha.toDouble())

private class TabBarDelegate(
    private val onItemSelected: (index: Int) -> Unit,
) : NSObject(), UITabBarDelegateProtocol {
    override fun tabBar(tabBar: UITabBar, didSelectItem: UITabBarItem) {
        onItemSelected(didSelectItem.tag.toInt())
    }
}
