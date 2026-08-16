plugins {
    alias(libs.plugins.base.kmp.compose.module)
}

kotlin {
    android {
        androidResources {
            enable = true
        }
    }

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
            implementation(projects.core)
            implementation(projects.coreUi)

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

            // GoogleAuthUiClient itself lives in :core now - this is just for the
            // CredentialManager type reference in Koin.android.kt's registration.
            implementation(libs.androidx.credentials)
        }
    }
}
