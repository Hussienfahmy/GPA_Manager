plugins {
    alias(libs.plugins.base.kmp.compose.module)
}

kotlin {
    // Kotlin/Native framework export: iosApp/ (the Xcode project) links against Shared.framework
    // and calls MainViewController() (shared/src/iosMain/.../MainViewController.kt) to get a
    // UIViewController hosting the whole Compose UI. Static (vs. dynamic) so Xcode just needs a
    // plain "Embed Frameworks" build phase, no run-script step. Unverified in this sandbox - no
    // Xcode/simulator available to confirm the framework actually links and runs.
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        // FLAG FOR REVIEW: everything here is androidMain, not commonMain. Two of its Navigation 3
        // deps (navigation3-ui, lifecycle-viewmodel-navigation3) are genuine Android-only AndroidX
        // artifacts with no iOS klib published ("KMP Dependencies Resolution Failure" when they
        // sat in commonMain against this module's iosArm64/iosSimulatorArm64 targets, added
        // globally to base_kmp_module in Phase 11). The composition root (AppNavHost/
        // OnboardingNavHost/AppNavigationState/the onboarding screens) is entirely Android Compose
        // code today with no iOS entry point calling it yet, so keeping the whole module
        // Android-only for now is honest, not a workaround - same "not attempted blind" stance as
        // the other Phase 11 iOS stubs. Revisit once there's a real iOS app shell and either
        // Navigation 3 or its own nav host to hand this off to.
        androidMain.dependencies {
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

            // FirebaseSyncRepository / FirebaseUserDataRepository (Firestore) and
            // FirebaseStorageRepository, moved here from :app in this phase.
            implementation(libs.gitlive.firebase.firestore)
            implementation(libs.gitlive.firebase.storage)

            // Replaces java.text.SimpleDateFormat/java.util.Date in the semester-history HTML
            // export's filename timestamp.
            implementation(libs.kotlinx.datetime)
        }
    }
}
