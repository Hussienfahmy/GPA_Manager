package com.h_fahmy.base

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

// Adds iosArm64/iosSimulatorArm64 alongside Android - applyDefaultHierarchyTemplate() below
// auto-creates iosMain/iosTest once these targets exist. iosX64 deliberately omitted.
// com.android.kotlin.multiplatform.library, not com.android.library: the latter is deprecated
// for KMP under AGP 9.
class BaseKmpModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        with(target) {
            pluginManager.apply {
                addPlugins(
                    libs = libs,
                    plugins = arrayOf(
                        "android.kotlin.multiplatform.library",
                        "kotlin.multiplatform",
                        "kotlin.serialization",
                        "ksp",
                    )
                )
            }

            val kotlinExtension = extensions.getByType<KotlinMultiplatformExtension>()

            // "android" is a plain Gradle extension here, not a member function - that sugar only
            // exists for .gradle.kts scripts, not plain Plugin<Project> classes like this one.
            (kotlinExtension as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("android") {
                namespace = libs.findVersion("nameSpace").get().toString()
                compileSdk {
                    version = release(libs.findVersion("compileSdk").get().toString().toInt()) {
                        minorApiLevel = 0
                    }
                }
                minSdk = libs.findVersion("minSdk").get().toString().toInt()

                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }

                // Off by default under this plugin - resources silently stop resolving without it.
                androidResources {
                    enable = true
                }

                // Replaces consumerProguardFiles("consumer-rules.pro"), silently dropped by this plugin.
                optimization {
                    consumerKeepRules.apply {
                        publish = true
                        file("consumer-rules.pro")
                    }
                }
            }

            kotlinExtension.apply {
                iosArm64()
                iosSimulatorArm64()

                applyDefaultHierarchyTemplate()

                sourceSets.commonMain.dependencies {
                    implementation(libs.getLibrary("koin-core"))
                    implementation(libs.getLibrary("kotlinx-serialization-json"))
                }
            }

            tasks.withType<KotlinCompilationTask<*>>().configureEach {
                compilerOptions {
                    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
                    // expect/actual classes are still formally Beta, warns on every build without this.
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
    }
}
