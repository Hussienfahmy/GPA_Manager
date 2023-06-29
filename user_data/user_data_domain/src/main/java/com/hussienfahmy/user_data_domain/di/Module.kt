package com.hussienfahmy.user_data_domain.di

import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienFahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienFahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienFahmy.core.domain.user_data.use_cases.ObserveUserData
import com.hussienfahmy.user_data_domain.use_cases.UpdateCreditHours
import com.hussienfahmy.user_data_domain.use_cases.UpdateCumulativeGPA
import com.hussienfahmy.user_data_domain.use_cases.UpdateDepartment
import com.hussienfahmy.user_data_domain.use_cases.UpdateEmail
import com.hussienfahmy.user_data_domain.use_cases.UpdateFaculty
import com.hussienfahmy.user_data_domain.use_cases.UpdateLevel
import com.hussienfahmy.user_data_domain.use_cases.UpdateName
import com.hussienfahmy.user_data_domain.use_cases.UpdatePhotoUrl
import com.hussienfahmy.user_data_domain.use_cases.UpdateSemester
import com.hussienfahmy.user_data_domain.use_cases.UpdateUniversity
import com.hussienfahmy.user_data_domain.use_cases.UploadPhoto
import com.hussienfahmy.user_data_domain.use_cases.UserDataUseCases
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
    fun provideUserDataUseCases(
        observeUserData: ObserveUserData,
        getUserData: GetUserData,
        getAcademicProgress: GetAcademicProgress,
        repository: UserDataRepository
    ) = UserDataUseCases(
        observeUserData = observeUserData,
        getUserData = getUserData,
        getAcademicProgress = getAcademicProgress,
        updateName = UpdateName(repository),
        updateEmail = UpdateEmail(repository),
        uploadPhoto = UploadPhoto(repository, UpdatePhotoUrl(repository)),
        updateUniversity = UpdateUniversity(repository),
        updateFaculty = UpdateFaculty(repository),
        updateDepartment = UpdateDepartment(repository),
        updateLevel = UpdateLevel(repository),
        updateSemester = UpdateSemester(repository),
        updateCumulativeGPA = UpdateCumulativeGPA(repository),
        updateCreditHours = UpdateCreditHours(repository),
    )
}