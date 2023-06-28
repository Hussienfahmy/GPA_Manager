package com.hussienfahmy.user_data_domain.use_cases

import com.hussienfahmy.user_data_domain.repository.UserDataRepository

class UpdatePhotoUrl(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke(photoUrl: String) = repository.updatePhotoUrl(photoUrl)
}
