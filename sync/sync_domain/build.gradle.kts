plugins {
    alias(libs.plugins.base.module)
}

dependencies {
    implementation(project(":core"))

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.firebase.firestore)
}