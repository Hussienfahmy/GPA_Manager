package com.hussienfahmy.user_data_presentaion

import com.hussienfahmy.user_data_domain.model.UserData

sealed class UserDataState {
    object Loading : UserDataState()
    data class Loaded(val userData: UserData) : UserDataState()
}
