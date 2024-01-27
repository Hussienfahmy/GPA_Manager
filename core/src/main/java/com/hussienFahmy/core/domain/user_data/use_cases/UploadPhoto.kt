package com.hussienFahmy.core.domain.user_data.use_cases

import android.net.Uri
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository

class UploadPhoto(
    private val repository: UserDataRepository,
    private val updatePhotoUrl: UpdatePhotoUrl,
) {
    suspend operator fun invoke(uri: Uri) {

    }
}
