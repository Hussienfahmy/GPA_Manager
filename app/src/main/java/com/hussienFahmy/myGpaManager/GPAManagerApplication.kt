package com.hussienfahmy.myGpaManager

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.hussienfahmy.myGpaManager.di.initKoin
import com.hussienfahmy.sync_domain.worker.SyncWorkerUpload
import org.koin.core.component.KoinComponent

class GPAManagerApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin(this)
        setupAppLifecycleObserver()
    }

    private fun setupAppLifecycleObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                val workManager = getKoin().get<WorkManager>()

                workManager.enqueueUniqueWork(
                    "upload_worker",
                    ExistingWorkPolicy.REPLACE,
                    SyncWorkerUpload.request
                )
            }
        })
    }
}