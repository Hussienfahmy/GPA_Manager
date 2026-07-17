plugins {
    alias(libs.plugins.base.kmp.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":gpa_system_settings:gpa_system_settings_domain"))

            implementation(libs.kotlinx.serialization.json)
        }
    }
}
