package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay

/**
 * Ported from the old Compose-Destinations DestinationStyle.Animated objects
 * (FadeTransitions/SlideTransitions/WelcomeSlideTransitions) to Navigation 3's metadata-based
 * transition API - NavDisplay.TransitionKey/PopTransitionKey put into a per-entry metadata map,
 * confirmed verbatim against developer.android.com's animate-destinations guide.
 *
 * FLAG FOR REVIEW: Nav3's docs don't fully spell out which entry's metadata (the one being
 * navigated to vs. the one being navigated back to) wins in which direction for a flat
 * multi-screen flow like onboarding - this is a best-effort port of the original visual timings,
 * not verified against a real build. Since these are purely cosmetic, worst case is a slightly
 * different animation, not a functional bug; spot-check the onboarding flow and the
 * History -> SemesterDetail push on a real device/emulator.
 */

private const val FADE_IN_DURATION = 220
private const val FADE_OUT_DURATION = 90
private const val FADE_IN_DELAY = 90
private const val SLIDE_DURATION = 300

/** Default fade-through transition, used for most top-level screens. */
val fadeTransitionMetadata: Map<String, Any> = metadata {
    put(NavDisplay.TransitionKey) {
        (fadeIn(tween(FADE_IN_DURATION, delayMillis = FADE_IN_DELAY)) +
                scaleIn(initialScale = 0.92f, animationSpec = tween(FADE_IN_DURATION, delayMillis = FADE_IN_DELAY))) togetherWith
                fadeOut(tween(FADE_OUT_DURATION))
    }
    put(NavDisplay.PopTransitionKey) {
        (fadeIn(tween(FADE_IN_DURATION, delayMillis = FADE_IN_DELAY)) +
                scaleIn(initialScale = 0.92f, animationSpec = tween(FADE_IN_DURATION, delayMillis = FADE_IN_DELAY))) togetherWith
                fadeOut(tween(FADE_OUT_DURATION))
    }
}

/** Horizontal slide, used for pushed detail/step screens (SemesterDetail, onboarding steps). */
val slideTransitionMetadata: Map<String, Any> = metadata {
    put(NavDisplay.TransitionKey) {
        (slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(SLIDE_DURATION)
        ) + fadeIn(tween(SLIDE_DURATION))) togetherWith
                (slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(SLIDE_DURATION)
                ) + fadeOut(tween(SLIDE_DURATION)))
    }
    put(NavDisplay.PopTransitionKey) {
        (slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth / 3 },
            animationSpec = tween(SLIDE_DURATION)
        ) + fadeIn(tween(SLIDE_DURATION))) togetherWith
                (slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(SLIDE_DURATION)
                ) + fadeOut(tween(SLIDE_DURATION)))
    }
}

/** Onboarding's welcome screen: no special enter, slides out to the left on forward nav. */
val welcomeSlideTransitionMetadata: Map<String, Any> = metadata {
    put(NavDisplay.TransitionKey) {
        EnterTransition.None togetherWith
                (slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(SLIDE_DURATION)
                ) + fadeOut(tween(SLIDE_DURATION)))
    }
    put(NavDisplay.PopTransitionKey) {
        EnterTransition.None togetherWith ExitTransition.None
    }
}
