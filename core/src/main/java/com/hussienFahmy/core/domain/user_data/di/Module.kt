package com.hussienfahmy.core.domain.user_data.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienfahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienfahmy.core.domain.user_data.use_cases.ObserveUserData
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateCreditHours
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateCumulativeGPA
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateDepartment
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateFaculty
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateLevel
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateName
import com.hussienfahmy.core.domain.user_data.use_cases.UpdatePhotoUrl
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateSemester
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateUniversity
import com.hussienfahmy.core.domain.user_data.use_cases.UploadPhoto
import com.hussienfahmy.core.domain.user_data.use_cases.UserDataUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @ViewModelScoped
    fun provideUserDataUseCases(
        observeUserData: ObserveUserData,
        getUserData: GetUserData,
        getAcademicProgress: GetAcademicProgress,
        repository: UserDataRepository,
        getGPASettings: GetGPASettings,
        @ApplicationContext context: Context
    ) = UserDataUseCases(
        observeUserData = observeUserData,
        getUserData = getUserData,
        getAcademicProgress = getAcademicProgress,
        updateName = UpdateName(repository),
        uploadPhoto = UploadPhoto(
            repository = repository,
            updatePhotoUrl = UpdatePhotoUrl(repository),
            storage = Firebase.storage,
            contentResolver = context.contentResolver
        ),
        updateUniversity = UpdateUniversity(repository),
        updateFaculty = UpdateFaculty(repository),
        updateDepartment = UpdateDepartment(repository),
        updateLevel = UpdateLevel(repository),
        updateSemester = UpdateSemester(repository),
        updateCumulativeGPA = UpdateCumulativeGPA(repository, getGPASettings),
        updateCreditHours = UpdateCreditHours(repository),
    )
}