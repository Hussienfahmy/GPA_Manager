package com.hussienfahmy.myGpaManager.data.storage

import dev.gitlive.firebase.storage.Data

actual fun ByteArray.toFirebaseStorageData(): Data = Data(this)
