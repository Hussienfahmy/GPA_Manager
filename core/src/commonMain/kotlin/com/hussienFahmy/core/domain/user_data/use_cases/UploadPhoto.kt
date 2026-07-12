package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.domain.storage.repository.StorageRepository
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.util.ImageThumbnailer
import com.hussienfahmy.core.util.PlatformImageSource
import kotlinx.coroutines.flow.first

class UploadPhoto(
    private val repository: UserDataRepository,
    private val updatePhotoUrl: UpdatePhotoUrl,
    private val storageRepository: StorageRepository,
    private val imageThumbnailer: ImageThumbnailer,
) {
    suspend operator fun invoke(source: PlatformImageSource) {
        val user = repository.userData.first() ?: return

        val thumbnail = imageThumbnailer.createThumbnail(source)
        val downloadUrl = storageRepository.uploadUserPhoto(user.id, thumbnail)

        updatePhotoUrl(downloadUrl)
    }
}