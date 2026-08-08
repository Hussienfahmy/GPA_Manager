plugins {
    alias(libs.plugins.base.kmp.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.gpaSystemSettings.gpaSystemSettingsDomain)

            implementation(libs.kotlinx.serialization.json)
        }
    }
}
