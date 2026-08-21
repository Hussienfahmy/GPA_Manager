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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalForeignApi::class)
@Composable
actual fun PlatformBottomNavContent(
    selectedRoute: AppRoute,
    colors: MeadowColors,
    onSelect: (BottomNavDestination) -> Unit,
) {
    val destinations = BottomNavDestination.entries
    val labels = destinations.map { stringResource(it.label) }
    val selectedIndex = destinations.indexOfFirst { it.route == selectedRoute }.coerceAtLeast(0)
    val selectedColor = destinations[selectedIndex].accent(colors).deep

    UIKitView(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative,
            placedAsOverlay = true,
        ),
        factory = {
            val tabBar = UITabBar().apply {
                itemPositioning = UITabBarItemPositioning.UITabBarItemPositioningCentered
            }
            applyAppearance(
                tabBar = tabBar,
                selectedColor = selectedColor,
                unselectedIconColor = colors.navItemIcon
            )

            val items = destinations.mapIndexed { index, destination ->
                val image = UIImage.systemImageNamed(destination.sfSymbol)
                    ?.imageWithRenderingMode(UIImageRenderingMode.UIImageRenderingModeAlwaysTemplate)
                UITabBarItem(title = labels[index], image = image, selectedImage = image).apply {
                    tag = index.toLong()
                }
            }
            tabBar.setItems(items, animated = false)
            tabBar.selectedItem = items.getOrNull(selectedIndex)

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
            applyAppearance(tabBar, selectedColor, colors.navItemIcon)
            val items = tabBar.items ?: return@UIKitView
            val current = items.getOrNull(selectedIndex) as? UITabBarItem
            if (tabBar.selectedItem != current) {
                tabBar.selectedItem = current
            }
        },
    )
}

private fun applyAppearance(tabBar: UITabBar, selectedColor: Color, unselectedIconColor: Color) {
    val appearance = UITabBarAppearance().apply {
        configureWithDefaultBackground()
        for (itemAppearance in listOf(stackedLayoutAppearance, inlineLayoutAppearance, compactInlineLayoutAppearance)) {
            itemAppearance.selected.iconColor = selectedColor.toUIColor()
            itemAppearance.selected.titleTextAttributes = mapOf(
                NSForegroundColorAttributeName to selectedColor.toUIColor()
            )
            itemAppearance.normal.iconColor = unselectedIconColor.toUIColor()
        }
    }
    tabBar.standardAppearance = appearance
    tabBar.scrollEdgeAppearance = appearance
    tabBar.tintColor = selectedColor.toUIColor()
}

private val BottomNavDestination.sfSymbol: String
    get() = when (this) {
        BottomNavDestination.Semester -> "calendar"
        BottomNavDestination.Marks -> "checkmark"
        BottomNavDestination.History -> "clock.arrow.circlepath"
        BottomNavDestination.Quick -> "sparkles"
        BottomNavDestination.More -> "ellipsis"
    }

internal fun Color.toUIColor(): UIColor =
    UIColor(red = red.toDouble(), green = green.toDouble(), blue = blue.toDouble(), alpha = alpha.toDouble())

private class TabBarDelegate(
    private val onItemSelected: (index: Int) -> Unit,
) : NSObject(), UITabBarDelegateProtocol {
    override fun tabBar(tabBar: UITabBar, didSelectItem: UITabBarItem) {
        onItemSelected(didSelectItem.tag.toInt())
    }
}
