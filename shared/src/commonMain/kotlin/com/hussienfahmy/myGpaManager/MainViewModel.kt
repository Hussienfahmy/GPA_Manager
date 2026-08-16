package com.hussienfahmy.myGpaManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.sync_domain.use_case.MigrateExistingUserDataIfNeeded
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch

class MainViewModel(
    authRepository: AuthRepository,
    private val migrateExistingUserDataIfNeeded: MigrateExistingUserDataIfNeeded,
) : ViewModel() {

    val isSignedIn = authRepository.isSignedInFlow

    init {
        viewModelScope.launch {
            val userId = Firebase.auth.currentUser?.uid
            if (userId != null) {
                migrateExistingUserDataIfNeeded(userId)
            }
        }
    }
}
