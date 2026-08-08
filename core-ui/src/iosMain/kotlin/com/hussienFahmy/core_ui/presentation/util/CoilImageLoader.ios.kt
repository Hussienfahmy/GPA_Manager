package com.hussienfahmy.core_ui.presentation.util

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcher
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

// Unlike Android (coil3-network-okhttp is auto-discovered via Coil's JVM-only ServiceLoader
// mechanism), Kotlin/Native has no such discovery - iOS needs an explicit SingletonImageLoader
// with a network fetcher registered, or every network-backed AsyncImage silently fails at
// runtime. Call this once from the iOS entry point before any Compose UI renders (see
// doInitApp() in :shared).
fun initCoilImageLoader() {
    SingletonImageLoader.setSafe { context: PlatformContext ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcher.factory(HttpClient(Darwin)))
            }
            .build()
    }
}
