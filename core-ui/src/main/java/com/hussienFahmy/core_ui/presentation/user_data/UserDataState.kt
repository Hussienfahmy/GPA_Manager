package com.hussienfahmy.core_ui.presentation.user_data

import com.hussienfahmy.core.domain.user_data.model.UserData

sealed class UserDataState {
    object Loading : UserDataState()
    data class Loaded(val userData: UserData, val uploadingPhoto: Boolean = false) : UserDataState()
}
