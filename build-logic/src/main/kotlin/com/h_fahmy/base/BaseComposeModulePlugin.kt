package com.h_fahmy.base

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class BaseComposeModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        with(target) {
            // apply core module
            pluginManager.apply {
                apply("com.gpa.base_module")
                addPlugins(
                    libs = libs,
                    plugins = arrayOf(
                        "kotlin-compose",
                    )
                )
            }

            dependencies {
                add("implementation", platform(libs.getLibrary("androidx.compose.bom")))
                addDependencies(
                    libs = libs,
                    dependencies = arrayOf(
                        "implementation" to "koin-androidx-compose",
                        "implementation" to "compose-icons-font-awesome",
                        "implementation" to "androidx-activity-compose",
                    )
                )

                addBundles(
                    libs = libs,
                    configurationNane = "implementation",
                    bundles = arrayOf(
                        "compose-presentation"
                    )
                )

                addBundles(
                    libs = libs,
                    configurationNane = "testImplementation",
                    bundles = arrayOf(
                        "test-android"
                    )
                )

                addBundles(
                    libs = libs,
                    configurationNane = "debugImplementation",
                    bundles = arrayOf(
                        "compose-debug"
                    )
                )

                addBundles(
                    libs = libs,
                    configurationNane = "implementation",
                    bundles = arrayOf(
                        "compose-debug"
                    )
                )
            }


            extensions.getByType<LibraryExtension>().apply {
                buildFeatures {
                    compose = true
                }
            }
        }
    }
}
