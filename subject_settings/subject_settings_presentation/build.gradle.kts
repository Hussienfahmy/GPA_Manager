plugins {
    alias(libs.plugins.base.module.compose)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":subject_settings:subject_settings_domain"))
}