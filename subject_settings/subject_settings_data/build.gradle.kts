plugins {
    alias(libs.plugins.base.module)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":subject_settings:subject_settings_domain"))
    implementation(libs.androidx.datastore.core)
    implementation(libs.kotlinx.serialization.json)
}