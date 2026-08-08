plugins {
    alias(libs.plugins.base.kmp.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.koin.androidx.worker)
        }
    }
}
