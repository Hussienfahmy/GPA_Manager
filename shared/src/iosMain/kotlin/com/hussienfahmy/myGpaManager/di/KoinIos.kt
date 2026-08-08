package com.hussienfahmy.myGpaManager.di

import com.hussienfahmy.core.domain.auth.service.AppleSignIn
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core.util.PlatformContext
import com.hussienfahmy.core_ui.presentation.util.initCoilImageLoader
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

// IosAuthService replaces firebaseModule's Android-only Google Sign-In binding.
actual fun platformModules(context: PlatformContext): List<Module> = listOf(
    module {
        single { context }
        single<AuthService> { IosAuthService(get()) }
        singleOf(::AppleSignIn)
    }
)

// Called once from the iosApp Xcode project's entry point (iosApp/iosApp/iOSApp.swift) before any
// Compose UI is shown.
//
// Named doInitKoin(), not initKoin(): Swift's Objective-C bridge treats any exported method whose
// name starts with "init" as an initializer, which would otherwise mangle how this is callable
// from Swift - the same reason JetBrains' own KMP project template avoids "init"-prefixed names.
fun doInitKoin() {
    initCoilImageLoader()
    initKoin(object : PlatformContext() {})
}
