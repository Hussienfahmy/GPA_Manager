plugins {
    alias(libs.plugins.base.kmp.compose.module)
}

android {
    namespace = "com.hussienfahmy.core_ui"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core"))

            // coil3.compose is genuinely multiplatform (unlike coil3.network.okhttp below), so
            // AsyncImage/ImageRequest/LocalPlatformContext now resolve on iOS too - this is what
            // unblocks UserPhoto.kt/MeadowUserCard.kt out of androidMain (Phase 11e).
            implementation(libs.coil3.compose)
        }

        // Android's network engine is auto-discovered via Coil's ServiceLoader mechanism (JVM-only
        // - Kotlin/Native has no such thing), so it stays a plain runtime dependency here and Coil
        // keeps working on Android exactly as before, zero behavior change. iOS has no network
        // engine wired yet: Kotlin/Native needs an explicit SingletonImageLoader.setSafe { ... }
        // with a coil3-network-ktor (Darwin Ktor engine) fetcher registered - deferred until the
        // iOS app entry point (iosApp/) exists to actually call that setup. Until then, network
        // photo loading on iOS will silently fail at runtime even though this compiles fine.
        androidMain.dependencies {
            implementation(libs.coil3.network.okhttp)
            implementation(libs.androidx.constraintlayout.compose)
        }
    }
}

// Internal to :core-ui - only MeadowType.kt references Res.font.* directly, no downstream module
// needs it, so this stays module-internal (no publicResClass override) unlike :core's Res class.
compose {
    resources {
        packageOfResClass = "com.hussienfahmy.core_ui.generated.resources"
    }
}