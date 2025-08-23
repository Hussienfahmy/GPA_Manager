plugins {
    alias(libs.plugins.base.module)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":sync:sync_domain"))

    implementation(libs.androidx.datastore.preferences)
}