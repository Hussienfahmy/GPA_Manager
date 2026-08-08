plugins {
    alias(libs.plugins.base.kmp.compose.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.coreUi)
            implementation(projects.gradesSetting.gradesSettingDomain)
        }
    }
}
