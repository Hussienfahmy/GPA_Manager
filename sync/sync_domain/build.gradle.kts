plugins {
    alias(libs.plugins.base.module)
}

dependencies {
    implementation(project(":core"))

    implementation(libs.firebase.firestore)
    implementation(libs.androidx.work.runtime.ktx)
}