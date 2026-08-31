package com.hussienfahmy.core.data.local.datastore

import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.core.util.PlatformContext
import kotlinx.coroutines.flow.first

/**
 * Persists a single "has this install ever completed a launch?" flag.
 *
 * The OS wipes every app DataStore/database on uninstall, so [hasLaunchedBefore] returning false
 * uniquely marks a fresh install. On iOS the Keychain-stored Firebase Auth session can outlive
 * the uninstall, so a fresh install may start already signed in with an empty local database -
 * DownloadDataOnFreshInstall uses this flag to pull the user's data back down in that case.
 */
class AppLifecycleDataStore(
    context: PlatformContext,
    crashReporter: CrashReporter,
) {
    private val dataSource = createDataStore(
        context = context,
        fileName = "app_lifecycle",
        serializer = AppLifecycleSerializer(crashReporter),
    )

    suspend fun hasLaunchedBefore(): Boolean = dataSource.data.first().hasLaunchedBefore

    suspend fun markLaunched() {
        dataSource.updateData { it.copy(hasLaunchedBefore = true) }
    }
}
