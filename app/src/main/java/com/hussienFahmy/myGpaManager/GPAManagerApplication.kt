package com.hussienfahmy.myGpaManager

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.hussienfahmy.myGpaManager.di.initKoin
import com.hussienfahmy.sync_domain.scheduler.BackgroundSyncScheduler
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
                getKoin().get<BackgroundSyncScheduler>().scheduleUploadSync()
            }
        })
    }
}