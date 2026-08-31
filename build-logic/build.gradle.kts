plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("base-kmp-module") {
            id = "com.gpa.base_kmp_module"
            implementationClass = "com.h_fahmy.base.BaseKmpModulePlugin"
        }

        create("base-kmp-compose-module") {
            id = "com.gpa.base_kmp_compose_module"
            implementationClass = "com.h_fahmy.base.BaseKmpComposeModulePlugin"
        }
    }
}

dependencies {
    compileClasspath(libs.gradle)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.compose.multiplatform.gradle.plugin)
}
