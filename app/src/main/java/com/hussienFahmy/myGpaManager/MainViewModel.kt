package com.hussienfahmy.myGpaManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.hussienfahmy.onboarding_presentation.sign_in.GoogleAuthUiClient
import com.hussienfahmy.sync_domain.worker.SyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val googleAuthUiClient: GoogleAuthUiClient,
    private val workManager: WorkManager,
) : ViewModel() {

    val isSignedIn = googleAuthUiClient.isSignedInFlow

    init {
        googleAuthUiClient.isSignedInFlow.filterNotNull().onEach { signedIn ->
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