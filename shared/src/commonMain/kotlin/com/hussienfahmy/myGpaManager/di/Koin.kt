package com.hussienfahmy.myGpaManager.di

import com.hussienfahmy.core.util.PlatformContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

// Per-platform extras (WorkManager, AuthService binding, PlatformContext single) not in sharedAppModules.
expect fun platformModules(context: PlatformContext): List<Module>

fun initKoin(context: PlatformContext) {
    startKoin {
        modules(sharedAppModules + platformModules(context))
    }
}
