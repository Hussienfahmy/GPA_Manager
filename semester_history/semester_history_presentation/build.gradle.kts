plugins {
    alias(libs.plugins.base.module.compose)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":semester_history:semester_history_domain"))
    implementation(project(":semester_subjctets:semester_subjctets_domain"))
    implementation(project(":semester_subjctets:semester_subjctets_presentaion"))
    implementation(project(":sync:sync_domain"))
}
