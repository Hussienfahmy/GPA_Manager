package com.hussienFahmy.core.domain.user_data.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.hussienFahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienFahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienFahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienFahmy.core.domain.user_data.use_cases.ObserveUserData
import com.hussienFahmy.core.domain.user_data.use_cases.UpdateCreditHours
import com.hussienFahmy.core.domain.user_data.use_cases.UpdateCumulativeGPA
import com.hussienFahmy.core.domain.user_data.use_cases.UpdateDepartment
import com.hussienFahmy.core.domain.user_data.use_cases.UpdateFaculty
import com.hussienFahmy.core.domain.user_data.use_cases.UpdateLevel
import com.hussienFahmy.core.domain.user_data.use_cases.UpdateName
import com.hussienFahmy.core.domain.user_data.use_cases.UpdatePhotoUrl
import com.hussienFahmy.core.domain.user_data.use_cases.UpdateSemester
import com.hussienFahmy.core.domain.user_data.use_cases.UpdateUniversity
import com.hussienFahmy.core.domain.user_data.use_cases.UploadPhoto
import com.hussienFahmy.core.domain.user_data.use_cases.UserDataUseCases
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