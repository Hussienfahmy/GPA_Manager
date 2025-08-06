plugins {
    alias(libs.plugins.base.module.compose)
}

android {
    namespace = "${libs.versions.appId.get()}.core_ui"
}

dependencies {
    implementation(project(":core"))
    implementation(libs.coil.compose)
}