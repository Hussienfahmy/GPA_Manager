package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.domain.storage.repository.StorageRepository
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import kotlinx.coroutines.flow.first

class UploadPhoto(
    private val repository: UserDataRepository,
    private val updatePhotoUrl: UpdatePhotoUrl,
    private val storageRepository: StorageRepository,
) {
    suspend operator fun invoke(source: PlatformFile) {
        val user = repository.userData.first() ?: return

        val thumbnail = FileKit.compressImage(file = source, quality = 90)
        val downloadUrl = storageRepository.uploadUserPhoto(user.id, thumbnail)

        updatePhotoUrl(downloadUrl)
    }
}