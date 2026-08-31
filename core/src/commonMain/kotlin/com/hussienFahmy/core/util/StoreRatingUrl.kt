package com.hussienfahmy.core.util

import org.jetbrains.compose.resources.StringResource

/** The platform's store listing to open for "rate the app," or null if there isn't one yet. */
expect fun storeRatingUrl(): String?

/** Localized display name of the platform's app store, for user-facing copy. */
expect val storeDisplayNameRes: StringResource
