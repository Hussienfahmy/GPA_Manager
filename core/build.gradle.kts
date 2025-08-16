plugins {
    alias(libs.plugins.base.module)
}

android {
    namespace = "${libs.versions.nameSpace.get()}.core"

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.firebase.storage)
}