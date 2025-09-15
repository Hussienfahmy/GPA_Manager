package com.hussienfahmy.myGpaManager.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateFCMToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class GPAFirebaseMessagingService : FirebaseMessagingService() {

    private val updateFCMToken: UpdateFCMToken by inject()
    private val scope: CoroutineScope by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        scope.launch {
            updateFCMToken(token)
        }
    }
}