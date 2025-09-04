plugins {
    alias(libs.plugins.base.module.compose)
}

android {
    namespace = "com.hussienfahmy.core_ui"
}

dependencies {
    implementation(project(":core"))
    implementation(libs.coil.compose)
}