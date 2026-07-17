plugins {
    alias(libs.plugins.base.kmp.compose.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-ui"))
            implementation(project(":quick:quick_domain"))
        }
    }
}
