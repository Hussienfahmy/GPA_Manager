plugins {
    alias(libs.plugins.base.kmp.compose.module)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.coreUi)
            implementation(projects.semesterHistory.semesterHistoryDomain)
            implementation(projects.semesterSubjctets.semesterSubjctetsDomain)
            implementation(projects.semesterSubjctets.semesterSubjctetsPresentaion)
            implementation(projects.sync.syncDomain)
        }
    }
}
