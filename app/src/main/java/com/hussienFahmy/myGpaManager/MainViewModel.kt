package com.hussienfahmy.myGpaManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.sync_domain.use_case.MigrateExistingUserDataIfNeeded
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
