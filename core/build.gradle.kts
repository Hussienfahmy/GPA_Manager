plugins {
    alias(libs.plugins.base.kmp.module)
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
        }

        androidMain.dependencies {
            implementation(libs.firebase.analytics)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.ktx)
            implementation(libs.koin.android)
        }
    }
}

dependencies {
    // Room's entities/DAOs/database now live in commonMain, but KSP still has to generate a
    // per-target implementation; "kspAndroid" is the only target processor needed until iOS is
    // added here, at which point kspIosArm64/kspIosSimulatorArm64/kspIosX64 join it.
    add("kspAndroid", libs.androidx.room.compiler)
}