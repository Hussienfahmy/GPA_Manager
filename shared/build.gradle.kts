plugins {
    alias(libs.plugins.base.kmp.compose.module)
}

kotlin {
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

            // Navigation 3 (JetBrains-republished multiplatform artifacts) - the app-level
            // composition root (AppNavHost/OnboardingNavHost/AppNavigationState) lives here so
            // both the Android and (future) iOS entry points can share it.
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)

            // Replaces java.text.SimpleDateFormat/java.util.Date in the semester-history HTML
            // export's filename timestamp.
            implementation(libs.kotlinx.datetime)
        }
    }
}
