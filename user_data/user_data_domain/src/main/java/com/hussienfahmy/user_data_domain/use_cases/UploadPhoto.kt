package com.hussienfahmy.user_data_domain.use_cases

import android.net.Uri
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository

class UploadPhoto(
    private val repository: UserDataRepository,
    private val updatePhotoUrl: UpdatePhotoUrl,
) {
    suspend operator fun invoke(uri: Uri) {
        // todo: upload photo to firebase storage
    }
}
