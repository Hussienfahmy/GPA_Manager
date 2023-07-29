package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import com.hussienFahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.hussienfahmy.gpa_system_sittings_presentaion.GPASettingsScreen
import com.ramcosta.composedestinations.annotation.Destination

@MoreNavGraph
@Destination
@Composable
fun AppGPASettingsScreen() {
    GPASettingsScreen()
}