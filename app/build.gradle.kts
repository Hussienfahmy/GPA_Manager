plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
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
            isShrinkResources = true
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

    implementation(project(":gpa_system_settings:gpa_system_settings_data"))
    implementation(project(":gpa_system_settings:gpa_system_settings_domain"))
    implementation(project(":gpa_system_settings:gpa_system_settings_presentaion"))

    implementation(project(":grades_setting:grades_setting_domain"))
    implementation(project(":grades_setting:grades_setting_presentation"))

    implementation(project(":onboarding:onboarding_presentation"))

    implementation(project(":quick:quick_domain"))
    implementation(project(":quick:quick_presentation"))

    implementation(project(":user_data:user_data_data"))

    implementation(project(":semester_marks:semester_marks_domain"))
    implementation(project(":semester_marks:semester_marks_presentaion"))

    implementation(project(":semester_subjctets:semester_subjctets_domain"))
    implementation(project(":semester_subjctets:semester_subjctets_presentaion"))

    implementation(project(":subject_settings:subject_settings_data"))
    implementation(project(":subject_settings:subject_settings_domain"))
    implementation(project(":subject_settings:subject_settings_presentation"))

    implementation(project(":sync:sync_data"))
    implementation(project(":sync:sync_domain"))

    implementation(project(":user_data:user_data_data"))

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.worker)

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

    // Baseline Profile
    implementation(libs.androidx.profileinstaller)

    // Firebase
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.firebase.messaging)


    // Authentication
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Debug
    debugImplementation(libs.bundles.compose.debug)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android testing
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}