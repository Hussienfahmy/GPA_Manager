package com.hussienfahmy.sync_domain.scheduler

import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.core.domain.sync.SyncUpload
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.BackgroundTasks.BGProcessingTask
import platform.BackgroundTasks.BGProcessingTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import platform.Foundation.NSDate
import platform.Foundation.dateWithTimeIntervalSinceNow

// Mirrors the identifier declared in Info.plist's BGTaskSchedulerPermittedIdentifiers - both must
// match exactly or registerForTaskWithIdentifier/submitTaskRequest silently fail at runtime.
private const val UPLOAD_SYNC_TASK_IDENTIFIER = "com.hussienFahmy.myGpaManager.uploadSync"

@OptIn(ExperimentalForeignApi::class)
actual class BackgroundSyncScheduler(
    private val syncUpload: SyncUpload,
    private val crashReporter: CrashReporter,
) {
    actual fun scheduleUploadSync() {
        val request = BGProcessingTaskRequest(identifier = UPLOAD_SYNC_TASK_IDENTIFIER)
        request.requiresNetworkConnectivity = true
        // Mirrors SyncWorkerUpload's 5-minute initial delay on Android - purely a hint, iOS
        // remains free to run this later (or not at all) based on its own opportunistic scheduling.
        request.earliestBeginDate = NSDate.dateWithTimeIntervalSinceNow(5.0 * 60.0)
        try {
            BGTaskScheduler.sharedScheduler.submitTaskRequest(request, error = null)
        } catch (e: Exception) {
            crashReporter.recordException(e, mapOf("operation" to "scheduleUploadSync"))
        }
    }

    // Unlike Android (WorkManager discovers workers via Koin's worker{} DSL with no explicit call
    // needed), BGTaskScheduler requires this registration to happen once, synchronously, before
    // the app finishes launching - called from doInitApp() in KoinIos.kt, which runs at the same
    // point iOSApp.swift's init() does.
    fun registerTaskHandler() {
        BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
            UPLOAD_SYNC_TASK_IDENTIFIER,
            usingQueue = null,
        ) { task ->
            if (task is BGProcessingTask) handleTask(task)
        }
    }

    private fun handleTask(task: BGProcessingTask) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            try {
                syncUpload()
                task.setTaskCompletedWithSuccess(true)
            } catch (e: Exception) {
                crashReporter.recordException(e, mapOf("operation" to "backgroundUploadSync"))
                task.setTaskCompletedWithSuccess(false)
            }
        }
        task.expirationHandler = { job.cancel() }
    }
}
