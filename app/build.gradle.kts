plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = libs.versions.nameSpace.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.appId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packagingOptions {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

dependencies {
    // Local modules
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":grades_setting:grades_setting_presentation"))
    // data module implemented so hilt can generate it's dependencies
    implementation(project(":user_data:user_data_data"))
    implementation(project(":quick:quick_presentation"))
    // data module implemented so hilt can generate it's dependencies
    implementation(project(":subject_settings:subject_settings_data"))
    implementation(project(":subject_settings:subject_settings_presentation"))
    implementation(project(":gpa_system_settings:gpa_system_settings_data"))
    implementation(project(":gpa_system_settings:gpa_system_settings_presentaion"))
    implementation(project(":semester_marks:semester_marks_presentaion"))
    implementation(project(":semester_subjctets:semester_subjctets_presentaion"))
    implementation(project(":onboarding:onboarding_presentation"))
    // data module implemented so hilt can generate it's dependencies
    implementation(project(":sync:sync_data"))
    implementation(project(":sync:sync_domain"))

    // Hilt
    implementation(libs.bundles.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)

    // Kotlin BOM
    implementation(platform(libs.kotlin.bom))

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    // Compose Destinations
    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)

    // Work Manager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.hilt.work)

    // Debug
    debugImplementation(libs.bundles.compose.debug)

    // Testing
    testImplementation(libs.junit)

    // Android testing
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}