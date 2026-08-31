package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.data.local.datastore.AppLifecycleDataStore
import com.hussienfahmy.core.domain.sync.SyncDownload

/**
 * On the first launch after a fresh install, pulls the signed-in user's data back down from
 * Firebase.
 *
 * iOS keeps the Firebase Auth session in the Keychain, which survives deleting the app, so a
 * reinstall silently restores the session (Google/Apple/email) with no sign-in prompt - but the
 * local Room database and DataStores were wiped, leaving the app signed in and empty. Detecting
 * that first launch via [AppLifecycleDataStore] and triggering [SyncDownload] repopulates the
 * local data without forcing the user to log out and back in.
 *
 * On Android a fresh install also clears the auth session, so [userId] is null here and this is a
 * no-op; the normal sign-in flow (SignInViewModel) downloads the data instead.
 */
class DownloadDataOnFreshInstall(
    private val appLifecycleDataStore: AppLifecycleDataStore,
    private val syncDownload: SyncDownload,
) {
    suspend operator fun invoke(userId: String?) {
        if (appLifecycleDataStore.hasLaunchedBefore()) return

        if (userId != null) {
            syncDownload(userId)
        }

        // Mark the install "launched" only after the restore (if any) succeeds - a failure here
        // (e.g. no network on first launch) leaves the flag unset so the next launch retries.
        appLifecycleDataStore.markLaunched()
    }
}
