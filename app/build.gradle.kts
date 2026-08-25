plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = libs.versions.nameSpace.get()
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt()) {
            minorApiLevel = 0
        }
    }

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

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("debug.jks")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
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
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

baselineProfile {
    // Only auto-generate on demand (./gradlew generateBaselineProfile), not on every release build.
    automaticGenerationDuringBuild = false
}

dependencies {
    // Local modules
    implementation(projects.shared)
    implementation(projects.core)
    implementation(projects.coreUi)

    // FileKit.init(this) in MainActivity - needed so filekit-dialogs (pulled in transitively via
    // :coreUi) has access to this Activity's ActivityResultRegistry for the gallery picker.
    implementation(libs.filekit.dialogs)

    implementation(projects.gpaSystemSettings.gpaSystemSettingsData)
    implementation(projects.gpaSystemSettings.gpaSystemSettingsDomain)
    implementation(projects.gpaSystemSettings.gpaSystemSettingsPresentaion)

    implementation(projects.gradesSetting.gradesSettingDomain)
    implementation(projects.gradesSetting.gradesSettingPresentation)

    implementation(projects.onboarding.onboardingPresentation)

    implementation(projects.quick.quickDomain)
    implementation(projects.quick.quickPresentation)

    implementation(projects.semesterMarks.semesterMarksDomain)
    implementation(projects.semesterMarks.semesterMarksPresentaion)

    implementation(projects.semesterSubjctets.semesterSubjctetsDomain)
    implementation(projects.semesterSubjctets.semesterSubjctetsPresentaion)

    implementation(projects.subjectSettings.subjectSettingsData)
    implementation(projects.subjectSettings.subjectSettingsDomain)
    implementation(projects.subjectSettings.subjectSettingsPresentation)

    implementation(projects.semesterHistory.semesterHistoryDomain)
    implementation(projects.semesterHistory.semesterHistoryPresentation)

    implementation(projects.sync.syncDomain)

    // Koin
    implementation(libs.koin.android)

    // Kotlin BOM
    implementation(platform(libs.kotlin.bom))

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.compose.debug)
    implementation(libs.bundles.compose.presentation)

    // Window size class for MainActivity's orientation lock (phone = portrait-locked, larger = free)
    implementation(libs.compose.material3.adaptive.navigation.suite)

    // Work Manager
    implementation(libs.androidx.work.runtime.ktx)

    // Baseline Profile
    implementation(libs.androidx.profileinstaller)
    baselineProfile(projects.baselineprofile)

    // Firebase (Firestore + Storage + Analytics + Auth all migrated to GitLive)
    implementation(platform(libs.firebase.bom))
    implementation(libs.gitlive.firebase.firestore)
    implementation(libs.gitlive.firebase.storage)
    implementation(libs.gitlive.firebase.analytics)
    implementation(libs.gitlive.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.perf)

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