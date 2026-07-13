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
        }

        // UserPhoto.kt / MeadowUserCard.kt (Coil 3) and everything depending on MeadowTheme's
        // font-based typography (MeadowType.kt uses top-level Font() calls, which need a
        // @Composable-scoped rewrite before they're commonMain-portable - deferred, see the
        // migration plan) stay androidMain-only for this phase.
        androidMain.dependencies {
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.okhttp)
            implementation(libs.androidx.constraintlayout.compose)
        }
    }
}