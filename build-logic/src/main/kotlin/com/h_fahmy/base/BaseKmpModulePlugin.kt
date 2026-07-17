package com.h_fahmy.base

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

// Android target only for now: iOS targets are added per-module once that module has real
// iosMain code to compile (see the migration plan's iOS phase, deliberately last). The old
// Android-only BaseModulePlugin/BaseComposeModulePlugin this coexisted with during the migration
// are gone now - every library module has migrated to this plugin (or its Compose counterpart).
class BaseKmpModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        with(target) {
            pluginManager.apply {
                addPlugins(
                    libs = libs,
                    plugins = arrayOf(
                        "android.library",
                        "kotlin.multiplatform",
                        "kotlin.serialization",
                        "ksp",
                    )
                )
            }

            extensions.getByType<LibraryExtension>().apply {
                namespace = libs.findVersion("nameSpace").get().toString()
                compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

                defaultConfig {
                    minSdk = libs.findVersion("minSdk").get().toString().toInt()
                    consumerProguardFiles("consumer-rules.pro")
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }

            extensions.getByType<KotlinMultiplatformExtension>().apply {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }
                }

                applyDefaultHierarchyTemplate()

                sourceSets.commonMain.dependencies {
                    implementation(libs.getLibrary("koin-core"))
                    implementation(libs.getLibrary("kotlinx-serialization-json"))
                }
            }

            tasks.withType<KotlinCompilationTask<*>>().configureEach {
                compilerOptions {
                    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
                }
            }
        }
    }
}
