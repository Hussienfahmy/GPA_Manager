package com.hussienfahmy.core.domain.auth.use_cases

import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.UserPropertyValues
import com.hussienfahmy.core.domain.auth.repository.AuthUserData
import com.hussienfahmy.core.domain.sync.SyncDownload
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.domain.user_data.use_cases.RefreshFcmToken

class CompleteSignIn(
    private val userDataRepository: UserDataRepository,
    private val analyticsLogger: AnalyticsLogger,
    private val syncDownload: SyncDownload,
    private val refreshFcmToken: RefreshFcmToken,
) {
    suspend operator fun invoke(userData: AuthUserData) {
        val isUserExists = userDataRepository.isUserExists()
        if (!isUserExists) {
            userDataRepository.createUserData(
                name = userData.name,
                email = userData.email,
                photoUrl = userData.photoUrl,
                id = userData.id,
            )
        }

        analyticsLogger.logSignInCompleted(userId = userData.id, isNewUser = !isUserExists)
        analyticsLogger.setUserType(
            if (!isUserExists) UserPropertyValues.USER_TYPE_NEW
            else UserPropertyValues.USER_TYPE_RETURNING
        )

        runCatching { refreshFcmToken() }

        syncDownload(userData.id)
    }
}
