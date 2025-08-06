plugins {
    alias(libs.plugins.base.module)
    alias(libs.plugins.google.services)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":sync:sync_domain"))

    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)

    implementation(libs.androidx.datastore.preferences)
}