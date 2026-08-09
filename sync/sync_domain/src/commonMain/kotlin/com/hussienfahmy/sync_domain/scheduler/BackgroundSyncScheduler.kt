package com.hussienfahmy.sync_domain.scheduler

// Deferred, guaranteed-eventually upload of local changes to Firebase, triggered when the app is
// backgrounded (see GPAManagerApplication.kt on Android, doInitApp() on iOS) so changes made right
// before backgrounding still reach the server even if the process is later killed. Android's actual
// wraps WorkManager (a real one-time work request, survives process death, waits for network);
// iOS's wraps BGTaskScheduler, which is inherently opportunistic - the OS decides if/when it runs,
// with no equivalent guarantee.
expect class BackgroundSyncScheduler {
    fun scheduleUploadSync()
}
