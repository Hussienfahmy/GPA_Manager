package com.hussienfahmy.user_data_data.di

import android.content.Context
import com.hussienfahmy.user_data_data.local.UserDataStore
import com.hussienfahmy.user_data_data.repository.UserDataRepositoryImpl
import com.hussienfahmy.user_data_domain.repository.UserDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object Module {

    @Provides
    @Singleton
    fun provideUserDataRepository(
        userDataStore: UserDataStore
    ): UserDataRepository = UserDataRepositoryImpl(userDataStore)

    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context: Context
    ): UserDataStore = UserDataStore(context)
}