package com.hussienfahmy.myGpaManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.sync_domain.use_case.MigrateExistingUserDataIfNeeded
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch

class MainViewModel(
    authService: AuthService,
    private val migrateExistingUserDataIfNeeded: MigrateExistingUserDataIfNeeded,
) : ViewModel() {

    val isSignedIn = authService.isSignedInFlow

    init {
        viewModelScope.launch {
            val userId = Firebase.auth.currentUser?.uid
            if (userId != null) {
                migrateExistingUserDataIfNeeded(userId)
            }
        }
    }
}
