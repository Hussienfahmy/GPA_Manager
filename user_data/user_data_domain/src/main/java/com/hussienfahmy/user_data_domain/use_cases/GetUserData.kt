package com.hussienfahmy.user_data_domain.use_cases

import com.hussienfahmy.user_data_domain.repository.UserDataRepository

class GetUserData(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke() = repository.getUserData()
}