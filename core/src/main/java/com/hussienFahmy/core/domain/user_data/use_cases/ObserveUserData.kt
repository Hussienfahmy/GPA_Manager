package com.hussienFahmy.core.domain.user_data.use_cases

import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository

class ObserveUserData(
    private val repository: UserDataRepository
) {
    operator fun invoke() = repository.observeUserData()
}