plugins {
    alias(libs.plugins.base.kmp.compose.module)
}

// google-services plugin only works on com.android.application - can't apply it here, so we
// parse :app's google-services.json directly and expose the OAuth web client ID as a BuildConfig
// field instead. Falls back to empty string if the (gitignored) file is missing.
android {
    buildFeatures {
        buildConfig = true
    }

    val googleServicesFile = rootProject.file("app/google-services.json")
    val webClientId = if (googleServicesFile.exists()) {
        val json = groovy.json.JsonSlurper().parse(googleServicesFile) as Map<*, *>
        @Suppress("UNCHECKED_CAST")
        val clients = json["client"] as List<Map<*, *>>
        val appClient = clients.first {
            val info = it["client_info"] as Map<*, *>
            val androidInfo = info["android_client_info"] as Map<*, *>
            androidInfo["package_name"] == libs.versions.appId.get()
        }
        @Suppress("UNCHECKED_CAST")
        val oauthClients = appClient["oauth_client"] as List<Map<*, *>>
        oauthClients.first { (it["client_type"] as Number).toInt() == 3 }["client_id"] as String
    } else {
        ""
    }

    defaultConfig {
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$webClientId\"")
    }
}

kotlin {
    // Kotlin/Native framework export: iosApp/ (the Xcode project) links against Shared.framework
    // and calls MainViewController() (shared/src/iosMain/.../MainViewController.kt) to get a
    // UIViewController hosting the whole Compose UI. Static (vs. dynamic) so Xcode just needs a
    // plain "Embed Frameworks" build phase, no run-script step.
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
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

            implementation(project(":semester_marks:semester_marks_domain"))
            implementation(project(":semester_marks:semester_marks_presentaion"))

            implementation(project(":semester_subjctets:semester_subjctets_domain"))
            implementation(project(":semester_subjctets:semester_subjctets_presentaion"))

            implementation(project(":subject_settings:subject_settings_data"))
            implementation(project(":subject_settings:subject_settings_domain"))
            implementation(project(":subject_settings:subject_settings_presentation"))

            implementation(project(":semester_history:semester_history_domain"))
            implementation(project(":semester_history:semester_history_presentation"))

            implementation(project(":sync:sync_domain"))

            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)

            // MainViewModel checks Firebase.auth.currentUser directly to decide whether to run
            // the one-time existing-user data migration.
            implementation(libs.gitlive.firebase.auth)

            // Used by FirebaseSyncRepository / FirebaseUserDataRepository (Firestore) and
            // FirebaseStorageRepository.
            implementation(libs.gitlive.firebase.firestore)
            implementation(libs.gitlive.firebase.storage)

            // Replaces java.text.SimpleDateFormat/java.util.Date in the semester-history HTML
            // export's filename timestamp.
            implementation(libs.kotlinx.datetime)

            // Notification permission request in GpaManagerApp.kt - rememberPermissionState()
            // handles lifecycle-aware permission state itself, no manual Activity wiring needed.
            implementation(libs.calf.permissions.core)
            implementation(libs.calf.permissions.notifications)
        }

        androidMain.dependencies {
            // Supplies versions for firebase-firestore/storage/auth (unversioned) that the
            // gitlive.firebase.* Android artifacts pull in transitively - an Android-only need,
            // since iOS's gitlive artifacts don't depend on Google's unversioned Android SDK.
            // project.dependencies.platform(...), not the bare platform(...) DSL extension - that
            // overload is broken inside KMP sourceSet dependency blocks under Kotlin 2.3 (KT-58759).
            implementation(project.dependencies.platform(libs.firebase.bom))

            // platformModules()'s Android actual replicates Koin's workManagerFactory() manually
            // (WorkManager.initialize + KoinWorkerFactory) since that call is KoinApplication
            // builder-scope only, not something a Module can express.
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.koin.androidx.worker)

            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services.auth)
            implementation(libs.googleid)
        }
    }
}
