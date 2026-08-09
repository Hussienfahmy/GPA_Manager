package com.hussienfahmy.myGpaManager.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclassesOfSealed

// rememberNavBackStack's common-code overload (the only one available outside Android, which
// instead has a reflection-based overload needing no configuration) requires every NavKey
// subtype to be registered up front for open polymorphic serialization of the back stack.
@OptIn(ExperimentalSerializationApi::class)
val navKeySavedStateConfiguration: SavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<AppRoute>()
            subclassesOfSealed<OnboardingRoute>()
        }
    }
}
