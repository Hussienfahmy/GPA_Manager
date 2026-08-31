plugins {
    alias(libs.plugins.base.kmp.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
        }
    }
}
