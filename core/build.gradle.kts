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
            // GitLive Firebase Analytics replaces the Android-only SDK dependency that used to
            // live in androidMain, now that FirebaseAnalyticsService/AnalyticsModule are
            // commonMain.
            implementation(libs.gitlive.firebase.analytics)
            // GitLive Firebase Crashlytics is genuinely multiplatform (Android + Apple), same as
            // analytics above - no expect/actual split needed for CrashReporter.
            implementation(libs.gitlive.firebase.crashlytics)
            // api: createDataStore()/OkioSerializer are part of :core's public API surface -
            // *_data modules' own DataStore-backed sources (GPADatastore,
            // SubjectSettingsDataSource) implement OkioSerializer<T> directly.
            api(libs.androidx.datastore.core.okio)
            api(libs.okio)
            // Replaces java.text.SimpleDateFormat/java.util.Date (no Kotlin/Native equivalent) in
            // the report-template renderers' date stamps.
            implementation(libs.kotlinx.datetime)
        }

        androidMain.dependencies {
            // Supplies the version for firebase-analytics (unversioned) that
            // gitlive.firebase.analytics's Android artifact pulls in transitively.
            // project.dependencies.platform(...), not the bare platform(...) DSL extension - that
            // overload is broken inside KMP sourceSet dependency blocks under Kotlin 2.3 (KT-58759).
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.ktx)
            implementation(libs.koin.android)
        }

        // AppleSignIn.kt exchanges its Apple ID credential for a Firebase session directly -
        // Android's equivalent (GoogleAuthUiClient/FirebaseAuthRepository) lives in :app instead,
        // since it also needs androidx.credentials, which :core doesn't otherwise depend on.
        iosMain.dependencies {
            implementation(libs.gitlive.firebase.auth)
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
    // per-target implementation - one processor per target that actually compiles this module.
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}