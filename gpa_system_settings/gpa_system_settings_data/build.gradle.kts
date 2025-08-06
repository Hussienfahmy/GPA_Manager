plugins {
    alias(libs.plugins.base.module)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":gpa_system_settings:gpa_system_settings_domain"))

    implementation(libs.androidx.datastore.core)
    implementation(libs.kotlinx.serialization.json)
}