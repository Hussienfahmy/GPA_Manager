package com.hussienfahmy.sync_domain.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.sync_domain.use_case.GetIsFirstTimeInstall
import com.hussienfahmy.sync_domain.use_case.PullSettings
import com.hussienfahmy.sync_domain.use_case.PullSubjects
import com.hussienfahmy.sync_domain.use_case.PushSettings
import com.hussienfahmy.sync_domain.use_case.PushSubjects
import com.hussienfahmy.sync_domain.use_case.SetIsFirstTimeInstall
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration

private const val TAG = "SyncWorker"

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val pullSubjects: PullSubjects,
    private val pushSubjects: PushSubjects,
    private val pushSettings: PushSettings,
    private val pullSettings: PullSettings,
    private val setIsFirstTimeInstall: SetIsFirstTimeInstall,
    private val getIsFirstTimeInstall: GetIsFirstTimeInstall,
) : CoroutineWorker(
    appContext,
    workerParams
) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val id = "sync_channel"
        val notificationId = 1

        createChannel()

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle("Sync Data")
            .setContentText("Fetch your subjects and settings...")
            .setSmallIcon(R.drawable.baseline_sync_24)
            .setOngoing(true)
            .build()

        return ForegroundInfo(notificationId, notification)
    }

    private fun createChannel() {
        val id = "sync_channel"
        val name = "Sync Channel"
        val channel = NotificationChannel(
            id,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override suspend fun doWork(): Result {
        if (getIsFirstTimeInstall()) {
            pullSubjects()
            pullSettings()
            setIsFirstTimeInstall(false)
        } else {
            pushSettings()
            pushSubjects()
        }
        return Result.success()
    }

    companion object {
        val downloadWorkRequest
            get() = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresCharging(false)
                        .setRequiresDeviceIdle(false)
                        .setRequiresBatteryNotLow(false)
                        .setRequiresStorageNotLow(false)
                        .build()
                )
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

        val uploadWorkRequest
            get() = PeriodicWorkRequestBuilder<SyncWorker>(1.days.toJavaDuration())
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(false)
                        .setRequiresCharging(false)
                        .setRequiresDeviceIdle(false)
                        .build()
                )
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    1,
                    TimeUnit.HOURS
                )
                .setInitialDelay(1.hours.toJavaDuration())
                .build()
    }
}