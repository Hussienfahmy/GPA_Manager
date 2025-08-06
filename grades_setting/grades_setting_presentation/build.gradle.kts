plugins {
    alias(libs.plugins.base.module.compose)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":grades_setting:grades_setting_domain"))
}