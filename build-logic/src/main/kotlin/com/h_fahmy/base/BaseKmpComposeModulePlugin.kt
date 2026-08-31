package com.h_fahmy.base

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

// Applies base_kmp_module first, then wires Compose Multiplatform + Koin's Compose Multiplatform
// artifacts into commonMain. Navigation 3 (androidx.navigation3) isn't wired in here since it's
// only used by :app's own navigation host, not by individual feature modules.
class BaseKmpComposeModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        with(target) {
            pluginManager.apply {
                apply("com.gpa.base_kmp_module")
                addPlugins(
                    libs = libs,
                    plugins = arrayOf(
                        "kotlin.compose",
                        "compose.multiplatform",
                    )
                )
            }

            extensions.getByType<KotlinMultiplatformExtension>().apply {
                sourceSets.commonMain.dependencies {
                    implementation(libs.getLibrary("compose-runtime"))
                    implementation(libs.getLibrary("compose-foundation"))
                    implementation(libs.getLibrary("compose-material3"))
                    implementation(libs.getLibrary("compose-material-icons-extended"))
                    implementation(libs.getLibrary("compose-components-resources"))
                    implementation(libs.getLibrary("compose-ui-tooling-preview"))
                    implementation(libs.getLibrary("koin-compose"))
                    implementation(libs.getLibrary("koin-compose-viewmodel"))
                }

                sourceSets.androidMain.dependencies {
                    implementation(libs.getLibrary("androidx-activity-compose"))
                }
            }

            // compose.uiToolingPreview's multiplatform @Preview needs this on the Android runtime
            // classpath too - com.android.kotlin.multiplatform.library (not com.android.library)
            // has no debugImplementation, so it goes on androidRuntimeClasspath directly instead.
            dependencies.add("androidRuntimeClasspath", libs.getLibrary("compose-ui-tooling"))
        }
    }
}
