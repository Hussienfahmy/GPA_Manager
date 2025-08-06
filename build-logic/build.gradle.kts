plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("base-module") {
            id = "com.gpa.base_module"
            implementationClass = "com.h_fahmy.base.BaseModulePlugin"
        }

        create("base-compose-module") {
            id = "com.gpa.base_compose_module"
            implementationClass = "com.h_fahmy.base.BaseComposeModulePlugin"
        }
    }
}

dependencies {
    compileClasspath(libs.gradle)
    implementation(libs.kotlin.gradle.plugin)
}
