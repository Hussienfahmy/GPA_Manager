package com.hussienfahmy.myGpaManager.di

import com.hussienfahmy.core.domain.auth.service.AppleSignIn
import com.hussienfahmy.core.domain.auth.service.EmailPasswordSignIn
import com.hussienfahmy.core.util.PlatformContext
import com.hussienfahmy.core_ui.presentation.util.initCoilImageLoader
import com.hussienfahmy.sync_domain.di.syncSchedulerModule
import com.hussienfahmy.sync_domain.scheduler.BackgroundSyncScheduler
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.mp.KoinPlatform
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidEnterBackgroundNotification

actual fun platformModules(context: PlatformContext): List<Module> = listOf(
    module {
        single { context }
        singleOf(::AppleSignIn)
        // Temporary, see EmailPasswordSignIn.kt - remove once Sign in with Apple is wired
        // back in as onboarding's "Get Started" action.
        singleOf(::EmailPasswordSignIn)
    },
    syncSchedulerModule,
)

// Called once from the iosApp Xcode project's entry point (iosApp/iosApp/iOSApp.swift) before any
// Compose UI is shown. Named doInitApp(), not initApp(): Swift's Objective-C bridge treats any
// exported method whose name starts with "init" as an initializer, which would otherwise mangle
// how this is callable from Swift - the same reason JetBrains' own KMP project template avoids
// "init"-prefixed names.
fun doInitApp() {
    // Unlike Android (auto-initialized by the Google Services Gradle plugin's ContentProvider),
    // iOS has no equivalent - FIRApp.configure() must be called explicitly before any Firebase
    // API is touched, reading GoogleService-Info.plist from the main bundle.
    Firebase.initialize(context = null)
    initCoilImageLoader()
    initKoin(object : PlatformContext() {})
    setupBackgroundSync()
}

// BGTaskScheduler's launch handler must be registered before the app finishes launching (this
// runs at the same point as iOSApp.swift's init()), unlike Android where WorkManager discovers
// its worker via Koin's worker{} DSL with no explicit call needed. The app-backgrounding trigger
// itself mirrors Android's ProcessLifecycleOwner.onStop() in GPAManagerApplication.kt.
private fun setupBackgroundSync() {
    val scheduler = KoinPlatform.getKoin().get<BackgroundSyncScheduler>()
    scheduler.registerTaskHandler()
    NSNotificationCenter.defaultCenter.addObserverForName(
        name = UIApplicationDidEnterBackgroundNotification,
        `object` = null,
        queue = null,
    ) {
        scheduler.scheduleUploadSync()
    }
}
