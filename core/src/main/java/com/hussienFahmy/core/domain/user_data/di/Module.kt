package com.hussienFahmy.core.domain.user_data.di

import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienFahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienFahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienFahmy.core.domain.user_data.use_cases.ObserveUserData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object Module {

    @Provides
    @ViewModelScoped
    fun provideObserveUserData(
        repository: UserDataRepository
    ) = ObserveUserData(repository)

    @Provides
    @ViewModelScoped
    fun provideGetUserData(
        repository: UserDataRepository
    ) = GetUserData(repository)

    @Provides
    @ViewModelScoped
    fun provideGetAcademicProgress(
        getUserData: GetUserData
    ) = GetAcademicProgress(getUserData)
}