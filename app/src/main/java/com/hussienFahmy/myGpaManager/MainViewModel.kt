package com.hussienfahmy.myGpaManager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core.domain.sync.SyncDownload
import com.hussienfahmy.sync_domain.use_case.GetIsInitialSyncDone
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    authService: AuthService,
    private val getIsInitialSyncDone: GetIsInitialSyncDone,
    private val syncDownload: SyncDownload
) : ViewModel() {

    val isSignedIn = authService.isSignedInFlow

    init {
        authService.isSignedInFlow.filterNotNull().onEach { signedIn ->
            Log.d("DEBUG_TAG", "isSignedIn = $signedIn")
            Log.d("DEBUG_TAG", "getIsInitialSyncDone = ${getIsInitialSyncDone()}")

            if (signedIn && !getIsInitialSyncDone()) {
                Log.d("DEBUG_TAG", "main view model will start download")
                syncDownload()
            }
        }.launchIn(viewModelScope)
    }
}