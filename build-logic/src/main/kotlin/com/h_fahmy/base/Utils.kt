package com.h_fahmy.base

import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun VersionCatalog.getLibrary(alias: String) = findLibrary(alias).get().get()

fun PluginManager.addPlugins(libs: VersionCatalog, plugins: Array<String>) {
    plugins.forEach {
        apply(libs.findPlugin(it).get().get().pluginId)
    }
}

fun DependencyHandlerScope.addDependencies(
    libs: VersionCatalog,
    dependencies: Array<Pair<String, String>>
) {
    dependencies.forEach { (configurationName, library) ->
        add(configurationName, libs.findLibrary(library).get().get())
    }
}

fun DependencyHandlerScope.addBundles(
    libs: VersionCatalog,
    configurationNane: String,
    bundles: Array<String>
) {
    bundles.forEach { bundle ->
        add(configurationNane, libs.findBundle(bundle).get())
    }
}