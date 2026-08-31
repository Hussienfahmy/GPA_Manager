package com.hussienfahmy.myGpaManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.sync_domain.use_case.DownloadDataOnFreshInstall
import com.hussienfahmy.sync_domain.use_case.MigrateExistingUserDataIfNeeded
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch

class MainViewModel(
    authRepository: AuthRepository,
    private val migrateExistingUserDataIfNeeded: MigrateExistingUserDataIfNeeded,
    private val downloadDataOnFreshInstall: DownloadDataOnFreshInstall,
) : ViewModel() {

    val isSignedIn = authRepository.isSignedInFlow

    init {
        viewModelScope.launch {
            val userId = Firebase.auth.currentUser?.uid

            // iOS restores the Keychain-stored Firebase session on reinstall, so the app can
            // start signed in with an empty local database - pull the data back down before
            // anything reads it.
            downloadDataOnFreshInstall(userId)

            if (userId != null) {
                migrateExistingUserDataIfNeeded(userId)
            }
        }
    }
}
