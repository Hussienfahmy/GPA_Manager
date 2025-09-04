package com.h_fahmy.base

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class BaseModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        with(target) {
            pluginManager.apply {
                addPlugins(
                    libs = libs,
                    plugins = arrayOf(
                        "android.library",
                        "kotlin.android",
                        "kotlin.parcelize",
                        "kotlin.serialization",
                        "ksp"
                    )
                )
            }

            dependencies {
                addDependencies(
                    libs = libs,
                    dependencies = arrayOf(
                        "implementation" to "koin-core",
                        "implementation" to "koin-android",
                        "implementation" to "koin-androidx-worker",
                    )
                )

                addBundles(
                    libs = libs,
                    configurationNane = "testImplementation",
                    bundles = arrayOf(
                        "test-unit"
                    )
                )

                addBundles(
                    libs = libs,
                    configurationNane = "androidTestImplementation",
                    bundles = arrayOf(
                        "test-android"
                    )
                )
            }


            extensions.getByType<LibraryExtension>().apply {
                namespace = libs.findVersion("nameSpace").get().toString()
                compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

                defaultConfig {
                    minSdk = libs.findVersion("minSdk").get().toString().toInt()
                    val targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                    lint.targetSdk = targetSdk
                    testOptions.targetSdk = targetSdk

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = "17"
                    freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
                }
            }
        }
    }
}
