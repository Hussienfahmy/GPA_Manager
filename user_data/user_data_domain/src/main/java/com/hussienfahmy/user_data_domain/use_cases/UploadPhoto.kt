package com.hussienfahmy.user_data_domain.use_cases

import android.net.Uri
import com.hussienfahmy.user_data_domain.repository.UserDataRepository

class UploadPhoto(
    private val repository: UserDataRepository,
    private val updatePhotoUrl: UpdatePhotoUrl,
) {
    suspend operator fun invoke(uri: Uri) {
        // todo: upload photo to firebase storage
    }
}
