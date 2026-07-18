package com.h_fahmy.base

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin
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

            val composeDependencies = ComposePlugin.Dependencies(target)

            extensions.getByType<KotlinMultiplatformExtension>().apply {
                sourceSets.commonMain.dependencies {
                    implementation(composeDependencies.runtime)
                    implementation(composeDependencies.foundation)
                    implementation(composeDependencies.material3)
                    implementation(composeDependencies.materialIconsExtended)
                    implementation(composeDependencies.components.resources)
                    implementation(composeDependencies.components.uiToolingPreview)
                    implementation(libs.getLibrary("koin-compose"))
                    implementation(libs.getLibrary("koin-compose-viewmodel"))
                }

                sourceSets.androidMain.dependencies {
                    implementation(libs.getLibrary("androidx-activity-compose"))
                }
            }
        }
    }
}
