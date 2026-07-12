package com.hussienfahmy.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import okio.FileSystem
import okio.Path

// Android actual in data/local/datastore/AppDataStoreFactory.android.kt; iOS actual added in the
// iOS phase. Mirrors DatabaseModule.kt's getDatabaseBuilder() pattern - no platform handle in the
// expect signature, Android's actual reaches into Koin's GlobalContext for the Application.
expect fun dataStoreFilePath(fileName: String): Path

/**
 * Shared multiplatform DataStore factory - replaces the Android-only Context.dataStore
 * delegate. Every *_data module's single-file settings DataStore (GPADatastore,
 * SubjectSettingsDataSource) should go through this rather than each rolling its own
 * OkioStorage/DataStoreFactory.create() call.
 */
fun <T> createDataStore(fileName: String, serializer: OkioSerializer<T>): DataStore<T> =
    DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = serializer,
            producePath = { dataStoreFilePath(fileName) },
        )
    )
