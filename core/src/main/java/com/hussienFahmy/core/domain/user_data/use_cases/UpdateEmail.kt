package com.hussienFahmy.core.domain.user_data.use_cases

import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository

class UpdateEmail(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke(email: String) = repository.updateEmail(email)
    // todo add validation
    // todo send verification email
}
