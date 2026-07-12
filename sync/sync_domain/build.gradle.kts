plugins {
    alias(libs.plugins.base.kmp.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
        }

        androidMain.dependencies {
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.koin.androidx.worker)
        }
    }
}
