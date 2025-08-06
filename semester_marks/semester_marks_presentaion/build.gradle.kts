plugins {
    alias(libs.plugins.base.module.compose)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":semester_marks:semester_marks_domain"))
}