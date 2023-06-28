package com.hussienfahmy.user_data_domain.use_cases

import com.hussienfahmy.user_data_domain.repository.UserDataRepository

class ObserveUserData(
    private val repository: UserDataRepository
) {
    operator fun invoke() = repository.observeUserData()
}