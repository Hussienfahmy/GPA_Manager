plugins {
    alias(libs.plugins.base.module)
    alias(libs.plugins.google.services)
}

dependencies {
    implementation(project(":core"))

    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
}