package com.hussienfahmy.user_data_presentaion

import com.hussienFahmy.core.domain.user_data.model.UserData

sealed class UserDataState {
    object Loading : UserDataState()
    data class Loaded(val userData: UserData) : UserDataState()
}
