plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = "com.hussienfahmy.baselineprofile"
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt()) {
            minorApiLevel = 0
        }
    }

    defaultConfig {
        minSdk = 28
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

// Baseline Profile generation must run against the "benchmark" build type that the
// androidx.baselineprofile plugin creates on :app (release-derived, but debuggable/profileable).
baselineProfile {
    useConnectedDevices = true
}

dependencies {
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}
