plugins {
    alias(libs.plugins.base.kmp.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":subject_settings:subject_settings_domain"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
