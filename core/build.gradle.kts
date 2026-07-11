plugins {
    alias(libs.plugins.base.kmp.module)
}

android {
    namespace = "com.hussienfahmy.core"

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.bundles.room)
            implementation(libs.firebase.analytics)
        }
    }
}

dependencies {
    // Room is still Android-only for this phase (its KMP move is a separate, dedicated phase);
    // "kspAndroid" is the per-target KSP configuration created for the androidTarget() by the
    // ksp + kotlin.multiplatform plugin combination.
    add("kspAndroid", libs.androidx.room.compiler)
}