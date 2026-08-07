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
            // unblocks UserPhoto.kt/MeadowUserCard.kt out of androidMain.
            implementation(libs.coil3.compose)
        }

        // Android's network engine is auto-discovered via Coil's ServiceLoader mechanism (JVM-only
        // - Kotlin/Native has no such thing), so it stays a plain runtime dependency here and Coil
        // keeps working on Android exactly as before, zero behavior change.
        androidMain.dependencies {
            implementation(libs.coil3.network.okhttp)
            implementation(libs.androidx.constraintlayout.compose)
        }

        // iOS has no ServiceLoader-style auto-discovery, so it needs an explicit
        // SingletonImageLoader.setSafe { ... } with a Ktor/Darwin-backed fetcher registered - see
        // CoilImageLoader.ios.kt, called from :shared's doInitKoin() now that iosApp/ exists.
        iosMain.dependencies {
            implementation(libs.coil3.network.ktor)
            implementation(libs.ktor.client.darwin)
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