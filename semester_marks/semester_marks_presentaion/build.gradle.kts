plugins {
    alias(libs.plugins.base.kmp.compose.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-ui"))
            implementation(project(":semester_marks:semester_marks_domain"))
        }
    }
}
