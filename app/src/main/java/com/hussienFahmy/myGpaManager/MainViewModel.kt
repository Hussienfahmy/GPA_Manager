package com.hussienfahmy.myGpaManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.sync_domain.worker.SyncWorker
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    authService: AuthService,
    private val workManager: WorkManager,
) : ViewModel() {

    val isSignedIn = authService.isSignedInFlow

    init {
        authService.isSignedInFlow.filterNotNull().onEach { signedIn ->
            if (signedIn) {
                workManager
                    .enqueueUniqueWork(
                        "download_worker",
                        ExistingWorkPolicy.REPLACE,
                        SyncWorker.downloadWorkRequest
                    )
            }

            workManager
                .enqueueUniquePeriodicWork(
                    "upload_worker",
                    ExistingPeriodicWorkPolicy.KEEP,
                    SyncWorker.uploadWorkRequest
                )
        }.launchIn(viewModelScope)
    }
}