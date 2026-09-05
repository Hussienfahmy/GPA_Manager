package com.hussienfahmy.myGpaManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.sync_domain.use_case.DownloadDataOnFreshInstall
import com.hussienfahmy.sync_domain.use_case.MigrateExistingUserDataIfNeeded
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    private val userDataRepository: UserDataRepository,
    private val migrateExistingUserDataIfNeeded: MigrateExistingUserDataIfNeeded,
    private val downloadDataOnFreshInstall: DownloadDataOnFreshInstall,
) : ViewModel() {

    val isSignedIn = authRepository.isSignedInFlow

    init {
        viewModelScope.launch {
            val userId = authRepository.userId.value

            // iOS restores the Keychain-stored Firebase session on reinstall, so the app can
            // start signed in with an empty local database - pull the data back down before
            // anything reads it.
            downloadDataOnFreshInstall(userId)

            if (userId != null) {
                if (authRepository.isAnonymousFlow.value == true) {
                    if (!userDataRepository.isUserExists()) {
                        userDataRepository.createUserData(id = userId, name = "", email = "", photoUrl = "")
                    }
                } else {
                    migrateExistingUserDataIfNeeded(userId)
                }
            }
        }
    }
}
