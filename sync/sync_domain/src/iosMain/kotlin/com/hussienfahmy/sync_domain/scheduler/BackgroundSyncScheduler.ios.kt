package com.hussienfahmy.sync_domain.scheduler

import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker
import com.hussienfahmy.core.domain.sync.SyncUpload
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import platform.BackgroundTasks.BGProcessingTask
import platform.BackgroundTasks.BGProcessingTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import platform.Foundation.NSBundle
import platform.Foundation.NSDate
import platform.Foundation.dateWithTimeIntervalSinceNow
import platform.UIKit.UIApplication
import platform.UIKit.UIBackgroundTaskInvalid

// Read from Info.plist instead of duplicating the identifier string here, so it can't drift.
private val UPLOAD_SYNC_TASK_IDENTIFIER: String by lazy {
    val identifiers = NSBundle.mainBundle
        .objectForInfoDictionaryKey("BGTaskSchedulerPermittedIdentifiers") as? List<*>
    identifiers?.firstOrNull() as? String
        ?: error("Info.plist is missing BGTaskSchedulerPermittedIdentifiers")
}

@OptIn(ExperimentalForeignApi::class)
actual class BackgroundSyncScheduler(
    private val syncUpload: SyncUpload,
    private val crashReporter: CrashReporter,
    private val dirtyTracker: SyncDirtyTracker,
) {
    actual fun scheduleUploadSync() {
        if (!dirtyTracker.hasAnyChanges()) return
        runImmediateUpload()
        scheduleFallback()
    }

    // beginBackgroundTask grants a guaranteed execution window immediately on backgrounding -
    // unlike BGTaskScheduler below, this isn't heuristic.
    private fun runImmediateUpload() {
        var taskId = UIBackgroundTaskInvalid
        taskId = UIApplication.sharedApplication.beginBackgroundTaskWithName("uploadSync") {
            UIApplication.sharedApplication.endBackgroundTask(taskId)
        }

        CoroutineScope(SupervisorJob()).launch {
            try {
                syncUpload()
            } catch (e: Exception) {
                crashReporter.recordException(e, mapOf("operation" to "immediateUploadSync"))
            } finally {
                UIApplication.sharedApplication.endBackgroundTask(taskId)
            }
        }
    }

    // Fallback in case the immediate attempt above doesn't finish in time or the app gets
    // suspended before it starts. Harmless no-op if it already succeeded, since SyncUploadImpl
    // only pushes collections still marked dirty.
    private fun scheduleFallback() {
        val request = BGProcessingTaskRequest(identifier = UPLOAD_SYNC_TASK_IDENTIFIER)
        request.requiresNetworkConnectivity = true
        request.earliestBeginDate = NSDate.dateWithTimeIntervalSinceNow(5.0)
        try {
            BGTaskScheduler.sharedScheduler.submitTaskRequest(request, error = null)
        } catch (e: Exception) {
            crashReporter.recordException(e, mapOf("operation" to "scheduleUploadSync"))
        }
    }

    // Unlike Android (WorkManager discovers workers via Koin's worker{} DSL), BGTaskScheduler
    // requires this registration to happen once, synchronously, before the app finishes launching.
    fun registerTaskHandler() {
        BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
            UPLOAD_SYNC_TASK_IDENTIFIER,
            usingQueue = null,
        ) { task ->
            if (task is BGProcessingTask) handleTask(task)
        }
    }

    private fun handleTask(task: BGProcessingTask) {
        val job = CoroutineScope(SupervisorJob()).launch {
            try {
                syncUpload()
                task.setTaskCompletedWithSuccess(true)
            } catch (e: Exception) {
                crashReporter.recordException(e, mapOf("operation" to "backgroundUploadSync"))
                task.setTaskCompletedWithSuccess(false)
            }
        }
        task.expirationHandler = {
            job.cancel()
        }
    }
}
