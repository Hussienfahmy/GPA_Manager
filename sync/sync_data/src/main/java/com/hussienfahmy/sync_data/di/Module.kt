package com.hussienfahmy.sync_data.di

import android.content.Context
import com.hussienfahmy.sync_data.datastore.AppDatastore
import com.hussienfahmy.sync_data.repository.AppDataRepositoryImpl
import com.hussienfahmy.sync_data.repository.SyncRepositoryImpl
import com.hussienfahmy.sync_domain.repository.AppDataRepository
import com.hussienfahmy.sync_domain.repository.SyncRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Provides
    @Singleton
    fun provideSyncRepository(): SyncRepository {
        return SyncRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideAppDatastore(
        @ApplicationContext context: Context
    ): AppDatastore {
        return AppDatastore(context)
    }

    @Singleton
    @Provides
    fun provideAppDataRepository(
        appDatastore: AppDatastore
    ): AppDataRepository {
        return AppDataRepositoryImpl(appDatastore)
    }
}