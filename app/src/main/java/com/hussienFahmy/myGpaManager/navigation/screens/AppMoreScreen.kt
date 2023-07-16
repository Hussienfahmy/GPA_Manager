package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.hussienFahmy.myGpaManager.viewmodels.MoreViewModel
import com.hussienfahmy.user_data_presentaion.components.UserInfoCard
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AppMoreScreen(
    onUserDataCardClick: () -> Unit
) {
    MoreScreen(onUserDataCardClick = onUserDataCardClick)
}

@Composable
private fun MoreScreen(
    moreViewModel: MoreViewModel = hiltViewModel(),
    onUserDataCardClick: () -> Unit
) {
    val userData = moreViewModel.userData
    userData?.let {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            UserInfoCard(onCardClick = onUserDataCardClick, userData = it)

        }
    }
}
