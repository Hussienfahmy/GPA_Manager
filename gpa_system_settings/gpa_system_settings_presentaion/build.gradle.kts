plugins {
    alias(libs.plugins.base.module.compose)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":gpa_system_settings:gpa_system_settings_domain"))
}