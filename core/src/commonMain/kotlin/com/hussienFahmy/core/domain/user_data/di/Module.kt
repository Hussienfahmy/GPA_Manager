package com.hussienfahmy.core.domain.user_data.di

import com.hussienfahmy.core.domain.auth.use_cases.CompleteSignIn
import com.hussienfahmy.core.domain.sample.SeedSampleData
import com.hussienfahmy.core.domain.user_data.use_cases.ClearFcmToken
import com.hussienfahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienfahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienfahmy.core.domain.user_data.use_cases.ObserveUserData
import com.hussienfahmy.core.domain.user_data.use_cases.RefreshFcmToken
import com.hussienfahmy.core.domain.user_data.use_cases.SignOut
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateCreditHours
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateCumulativeGPA
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateDepartment
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateFCMToken
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateFaculty
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateLevel
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateName
import com.hussienfahmy.core.domain.user_data.use_cases.UpdatePhotoUrl
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateSemester
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateUniversity
import com.hussienfahmy.core.domain.user_data.use_cases.UploadPhoto
import com.hussienfahmy.core.domain.user_data.use_cases.UserDataUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreUserDataDomainModule = module {
    singleOf(::ObserveUserData)
    singleOf(::GetUserData)
    singleOf(::GetAcademicProgress)
    singleOf(::UpdateLevel)
    singleOf(::UpdateSemester)
    singleOf(::UpdateName)
    singleOf(::UpdatePhotoUrl)
    singleOf(::UploadPhoto)
    singleOf(::UpdateUniversity)
    singleOf(::UpdateFaculty)
    singleOf(::UpdateDepartment)
    singleOf(::UpdateCumulativeGPA)
    singleOf(::UpdateCreditHours)
    singleOf(::UserDataUseCases)
    singleOf(::SignOut)
    singleOf(::UpdateFCMToken)
    singleOf(::RefreshFcmToken)
    singleOf(::ClearFcmToken)
    singleOf(::CompleteSignIn)
    singleOf(::SeedSampleData)
}