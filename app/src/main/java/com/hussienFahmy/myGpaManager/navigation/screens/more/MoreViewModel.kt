package com.hussienFahmy.myGpaManager.navigation.screens.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienFahmy.core.domain.user_data.model.UserData
import com.hussienFahmy.core.domain.user_data.use_cases.GetUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    getUserData: GetUserData
) : ViewModel() {

    var userData by mutableStateOf<UserData?>(null)

    init {
        viewModelScope.launch {
            userData = getUserData()
        }
    }
}