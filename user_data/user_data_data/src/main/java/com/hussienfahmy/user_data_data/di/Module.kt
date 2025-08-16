package com.hussienfahmy.user_data_data.di

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.user_data_data.repository.UserDataApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object Module {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        val settings = firestoreSettings {
            setLocalCacheSettings(persistentCacheSettings {
                setSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            })
        }

        val db = Firebase.firestore
        db.firestoreSettings = settings
        return db
    }

    @Provides
    @Singleton
    fun provideUserDataApiService(
        db: FirebaseFirestore,
    ): UserDataRepository {
        return UserDataApiService(
            db = db,
            auth = Firebase.auth
        )
    }
}