package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

/**
 * Friendly fade-through transitions (better for bottom navigation and most screens).
 * No extra dependency required (uses Compose animation APIs).
 */
object FadeTransitions : DestinationStyle.Animated() {

    private const val IN_DURATION = 220
    private const val OUT_DURATION = 90
    private const val IN_DELAY = 90

    override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
        {
            fadeIn(animationSpec = tween(durationMillis = IN_DURATION, delayMillis = IN_DELAY)) +
                    scaleIn(
                        initialScale = 0.92f,
                        animationSpec = tween(durationMillis = IN_DURATION, delayMillis = IN_DELAY)
                    )
        }

    override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
        {
            fadeOut(animationSpec = tween(durationMillis = OUT_DURATION))
        }

    override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
        {
            fadeIn(animationSpec = tween(durationMillis = IN_DURATION, delayMillis = IN_DELAY)) +
                    scaleIn(
                        initialScale = 0.92f,
                        animationSpec = tween(durationMillis = IN_DURATION, delayMillis = IN_DELAY)
                    )
        }

    override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
        {
            fadeOut(animationSpec = tween(durationMillis = OUT_DURATION))
        }
}
