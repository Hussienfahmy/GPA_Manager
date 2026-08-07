package com.hussienfahmy.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import com.hussienfahmy.core.util.PlatformContext
import okio.FileSystem
import okio.Path
import okio.SYSTEM

expect fun dataStoreFilePath(context: PlatformContext, fileName: String): Path

/**
 * Shared multiplatform DataStore factory - replaces the Android-only Context.dataStore
 * delegate. Every *_data module's single-file settings DataStore (GPADatastore,
 * SubjectSettingsDataSource) should go through this rather than each rolling its own
 * OkioStorage/DataStoreFactory.create() call.
 */
fun <T> createDataStore(
    context: PlatformContext,
    fileName: String,
    serializer: OkioSerializer<T>,
): DataStore<T> =
    DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = serializer,
            producePath = { dataStoreFilePath(context, fileName) },
        )
    )
