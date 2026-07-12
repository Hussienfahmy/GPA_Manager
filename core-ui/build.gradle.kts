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

        // UserPhoto.kt / MeadowUserCard.kt (Coil 3) and UserCardInfo.kt (ConstraintLayout
        // Compose, no CMP port) stay androidMain-only - MeadowType.kt's font blocker is resolved
        // (Phase 11), everything else in this module is commonMain now.
        androidMain.dependencies {
            implementation(libs.coil3.compose)
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