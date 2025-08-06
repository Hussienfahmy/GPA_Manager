plugins {
    alias(libs.plugins.base.module.compose)
    alias(libs.plugins.google.services)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))

    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
}