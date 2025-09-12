package com.hussienfahmy.myGpaManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core.domain.sync.SyncDownload
import com.hussienfahmy.sync_domain.use_case.GetIsInitialSyncDone
import com.hussienfahmy.sync_domain.worker.SyncWorkerUpload
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    authService: AuthService,
    private val workManager: WorkManager,
    private val getIsInitialSyncDone: GetIsInitialSyncDone,
    private val syncDownload: SyncDownload
) : ViewModel() {

    val isSignedIn = authService.isSignedInFlow

    init {
        authService.isSignedInFlow.filterNotNull().onEach { signedIn ->
            if (signedIn && !getIsInitialSyncDone()) {
                syncDownload()
            }

            workManager
                .enqueueUniquePeriodicWork(
                    "upload_worker",
                    ExistingPeriodicWorkPolicy.KEEP,
                    SyncWorkerUpload.request
                )
        }.launchIn(viewModelScope)
    }
}