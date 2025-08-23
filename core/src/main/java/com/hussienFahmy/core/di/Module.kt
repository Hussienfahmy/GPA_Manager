package com.hussienfahmy.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

object CoreQualifiers {
    const val DEFAULT_DISPATCHER = "defaultDispatcher"
}

val coreModule = module {
    single<CoroutineDispatcher>(named(CoreQualifiers.DEFAULT_DISPATCHER)) { Dispatchers.Default }
    single<CoroutineScope> { CoroutineScope(Dispatchers.Main + SupervisorJob()) }
}