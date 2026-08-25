package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

private val TOP_LEVEL_ROUTES: List<AppRoute> = listOf(
    AppRoute.Semester,
    AppRoute.SemesterMarks,
    AppRoute.SemesterHistory,
    AppRoute.Quick,
    AppRoute.More,
)

/**
 * Adapts Google's official multi-back-stack Navigation 3 recipe
 * (android/nav3-recipes, multiplestacks/NavigationState.kt + Navigator.kt) to this app's 5
 * bottom-nav tabs. Each tab keeps its own back stack - so History's pushed SemesterDetail (or
 * More's pushed settings screens) survive switching to another tab and back - and only the
 * start tab (Semester) plus the currently-active tab are ever rendered/composed at once
 * ("exit through home": the user always exits the app via the Semester tab, matching the old
 * Compose Destinations popUpTo(root.startDestination) behavior).
 *
 * `topLevelRoute` survives process death and Activity recreation (e.g. the orientation lock in
 * MainActivity flipping the width breakpoint) via rememberSaveable, saved as its index into
 * TOP_LEVEL_ROUTES since every entry is a plain `data object` with nothing else to serialize.
 * Each tab's own back stack content (e.g. a pushed SemesterDetail) survives the same way via
 * rememberNavBackStack itself.
 */
private val TopLevelRouteSaver = Saver<MutableState<AppRoute>, Int>(
    save = { TOP_LEVEL_ROUTES.indexOf(it.value) },
    restore = { mutableStateOf(TOP_LEVEL_ROUTES[it]) },
)

@Composable
fun rememberAppNavigationState(): AppNavigationState {
    val topLevelRouteState = rememberSaveable(saver = TopLevelRouteSaver) {
        mutableStateOf(AppRoute.Semester)
    }
    val backStacks: Map<AppRoute, NavBackStack<NavKey>> = TOP_LEVEL_ROUTES.associateWith { route ->
        rememberNavBackStack(navKeySavedStateConfiguration, route)
    }

    return remember {
        AppNavigationState(
            startRoute = AppRoute.Semester,
            topLevelRouteState = topLevelRouteState,
            backStacks = backStacks,
        )
    }
}

class AppNavigationState(
    val startRoute: AppRoute,
    private val topLevelRouteState: MutableState<AppRoute>,
    val backStacks: Map<AppRoute, NavBackStack<NavKey>>,
) {

    var topLevelRoute: AppRoute
        get() = topLevelRouteState.value
        set(value) {
            topLevelRouteState.value = value
        }

    /** Switches tabs, or pushes onto the current tab's stack if `route` isn't a top-level tab. */
    fun navigate(route: AppRoute) {
        if (route in backStacks.keys) {
            topLevelRoute = route
        } else {
            backStacks[topLevelRoute]?.add(route)
        }
    }

    /**
     * Bottom-nav tap handling: switches to the tapped tab, or - if it's already the active tab -
     * resets that tab's stack back to its root screen (mirrors the old Nav2
     * popUpTo(start){saveState=true}+launchSingleTop behavior for re-tapping the current tab).
     */
    fun onTabSelected(route: AppRoute) {
        if (route == topLevelRoute) {
            backStacks[route]?.let { stack ->
                while (stack.size > 1) stack.removeLastOrNull()
            }
        } else {
            topLevelRoute = route
        }
    }

    /** True if the back press was consumed; false means the caller (system back) should exit. */
    fun goBack(): Boolean {
        val currentStack = backStacks[topLevelRoute] ?: return false
        return if (currentStack.size <= 1) {
            if (topLevelRoute != startRoute) {
                topLevelRoute = startRoute
                true
            } else {
                false
            }
        } else {
            currentStack.removeLastOrNull()
            true
        }
    }

    /**
     * Builds the flattened, decorated entry list NavDisplay renders. Every tab's back stack is
     * decorated on every call (constant composable-call count, required by Compose), then only
     * the active tabs' entries are kept - mirroring the official recipe's
     * `toDecoratedEntries` exactly.
     */
    @Composable
    fun entries(entryProvider: (NavKey) -> NavEntry<NavKey>): List<NavEntry<NavKey>> {
        val decoratedEntries = backStacks.mapValues { (_, stack) ->
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider,
            )
        }

        val activeRoutes = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
        return activeRoutes.flatMap { decoratedEntries[it] ?: emptyList() }
    }
}
