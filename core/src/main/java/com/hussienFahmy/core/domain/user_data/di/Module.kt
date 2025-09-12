package com.hussienfahmy.core.domain.user_data.di

import android.content.Context
import com.hussienfahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienfahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienfahmy.core.domain.user_data.use_cases.ObserveUserData
import com.hussienfahmy.core.domain.user_data.use_cases.SignOut
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
import org.koin.dsl.module

val coreUserDataDomainModule = module {
    single { ObserveUserData(get()) }
    single { GetUserData(get()) }
    single { GetAcademicProgress(get()) }

    single {
        UserDataUseCases(
            observeUserData = get(),
            getUserData = get(),
            getAcademicProgress = get(),
            updateName = UpdateName(get()),
            uploadPhoto = UploadPhoto(
                repository = get(),
                updatePhotoUrl = UpdatePhotoUrl(get()),
                storageRepository = get(),
                contentResolver = get<Context>().contentResolver
            ),
            updateUniversity = UpdateUniversity(get()),
            updateFaculty = UpdateFaculty(get()),
            updateDepartment = UpdateDepartment(get()),
            updateLevel = UpdateLevel(get()),
            updateSemester = UpdateSemester(get()),
            updateCumulativeGPA = UpdateCumulativeGPA(get(), get()),
            updateCreditHours = UpdateCreditHours(get()),
        )
    }
    single {
        SignOut(
            authService = get(),
            subjectDao = get(),
            syncUpload = get(),
            setIsInitialSyncDone = get(),
        )
    }
}