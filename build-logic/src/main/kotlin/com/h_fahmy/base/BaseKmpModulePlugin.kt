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
// auto-creates the iosMain/iosTest intermediate source sets once these targets exist, no
// per-module wiring needed. iosX64 (Intel simulator) is deliberately omitted.
//
// Uses com.android.kotlin.multiplatform.library, not com.android.library: the latter is
// deprecated for KMP modules under AGP 9 (kotlin.multiplatform + com.android.library triggers a
// removal warning, gone entirely in AGP 10). The new plugin folds Android config into
// kotlin { android { ... } } instead of a separate top-level android {} / LibraryExtension.
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

            // com.android.kotlin.multiplatform.library registers its "android" DSL as a plain
            // Gradle extension on KotlinMultiplatformExtension, not a compiled-in member function -
            // that sugar only exists for .gradle.kts precompiled script plugins via generated
            // type-safe accessors, which this plain Plugin<Project> class doesn't get.
            (kotlinExtension as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("android") {
                namespace = libs.findVersion("nameSpace").get().toString()
                // compileSdk catalog entry is "37.0" (used to feed compileSdkVersion("android-37.0")
                // under the old string-based API) - the new compileSdk property is a plain Int API
                // level, so only the major component before the dot is relevant.
                compileSdk = libs.findVersion("compileSdk").get().toString().substringBefore(".").toInt()
                minSdk = libs.findVersion("minSdk").get().toString().toInt()

                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }

                // androidResources defaults to disabled under this plugin - res/ and Compose
                // Multiplatform resources alike silently stop resolving on the Android target
                // without this (no compile error, just missing resources at runtime).
                androidResources {
                    enable = true
                }

                // consumerProguardFiles("consumer-rules.pro") on the old LibraryExtension is
                // silently dropped by this plugin - equivalent is optimization.consumerKeepRules,
                // with publish = true so :app's R8 pass actually picks these rules up from the
                // downstream library dependency (not implicit like the old API).
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
                }
            }
        }
    }
}
