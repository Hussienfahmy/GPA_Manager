package com.hussienfahmy.core.data.local.datastore

import kotlinx.serialization.Serializable

/**
 * Persisted, single-boolean marker for "the app has completed a launch on this install".
 *
 * The OS wipes every app DataStore/database on uninstall, so [hasLaunchedBefore] == false
 * uniquely identifies a fresh install - including an iOS reinstall where the Keychain-persisted
 * Firebase Auth session outlived the deletion but the local data did not. See
 * DownloadDataOnFreshInstall.
 */
@Serializable
internal data class AppLifecycleState(
    val hasLaunchedBefore: Boolean = false,
)
