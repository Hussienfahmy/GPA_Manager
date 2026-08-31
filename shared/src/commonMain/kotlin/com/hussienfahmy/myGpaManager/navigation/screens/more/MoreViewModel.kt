package com.hussienfahmy.myGpaManager.navigation.screens.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienfahmy.core.domain.user_data.use_cases.SignOut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MoreViewModel(
    getUserData: GetUserData,
    private val signOutUseCase: SignOut,
    private val analyticsLogger: AnalyticsLogger,
    private val applicationScope: CoroutineScope
) : ViewModel() {

    val userData = getUserData()

    var isSigningOut by mutableStateOf(false)
        private set

    fun signOut() {
        if (isSigningOut) return
        applicationScope.launch {
            try {
                isSigningOut = true
                signOutUseCase()
                analyticsLogger.logSignOut()
            } finally {
                isSigningOut = false
            }
        }
    }

    fun logAppRatingClicked() {
        analyticsLogger.logAppRatingClicked()
    }
}