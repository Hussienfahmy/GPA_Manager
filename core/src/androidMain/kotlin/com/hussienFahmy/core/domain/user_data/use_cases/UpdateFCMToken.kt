package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository

class UpdateFCMToken(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke(fcmToken: String) {
        repository.updateFCMToken(fcmToken)
    }
}
