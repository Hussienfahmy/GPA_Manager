package com.hussienfahmy.myGpaManager.navigation.screens.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienfahmy.core.domain.user_data.use_cases.SignOut
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MoreViewModel(
    getUserData: GetUserData,
    private val signOutUseCase: SignOut
) : ViewModel() {

    var userData by mutableStateOf<UserData?>(null)
        private set

    var isSigningOut by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            userData = getUserData().filterNotNull().first()
        }
    }

    fun signOut() {
        if (isSigningOut) return
        viewModelScope.launch {
            try {
                isSigningOut = true
                signOutUseCase()
                userData = null
            } finally {
                isSigningOut = false
            }
        }
    }
}