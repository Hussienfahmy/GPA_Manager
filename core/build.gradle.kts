plugins {
    alias(libs.plugins.base.module)
}

android {
    namespace = "${libs.versions.appId.get()}.core"

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}