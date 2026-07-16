plugins {
    alias(libs.plugins.base.kmp.module)
    // Needed for UiText's @Composable asString() and the generated Res.string.* accessors below -
    // :core does NOT pull in compose.foundation/material3/ui, just the compiler plugin + the
    // resources library, so it stays a data/domain module rather than a UI module.
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
}

android {
    namespace = "com.hussienfahmy.core"

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
        // Room KMP needs its generated _Impl classes in Kotlin, not Java, to target non-JVM
        // platforms. Harmless on Android-only today; required once iosMain is added here.
        arg("room.generateKotlin", "true")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.room)
            implementation(libs.androidx.sqlite.bundled)
            // api, not implementation: UiText.Resource exposes StringResource as part of :core's
            // public API surface, and ~70 downstream modules need Res.string.* to resolve.
            api(compose.components.resources)
            // compose.components.resources only brings the resources runtime; UiText.asString()
            // is itself @Composable, which needs the Compose runtime (Composable annotation,
            // stringResource) on the classpath too.
            api(compose.runtime)
        }

        androidMain.dependencies {
            implementation(libs.firebase.analytics)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.ktx)
            implementation(libs.koin.android)
        }
    }
}

// Res needs to be public (default is module-internal) since dozens of downstream Android-only
// modules reference com.hussienfahmy.core.generated.resources.Res.string.* directly, and a fixed
// package name keeps their imports stable regardless of :core's own namespace.
compose {
    resources {
        publicResClass = true
        packageOfResClass = "com.hussienfahmy.core.generated.resources"
    }
}

dependencies {
    // Room's entities/DAOs/database now live in commonMain, but KSP still has to generate a
    // per-target implementation; "kspAndroid" is the only target processor needed until iOS is
    // added here, at which point kspIosArm64/kspIosSimulatorArm64/kspIosX64 join it.
    add("kspAndroid", libs.androidx.room.compiler)
}