package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import com.hussienFahmy.myGpaManager.navigation.SlideTransitions
import com.hussienFahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.hussienfahmy.gpa_system_sittings_presentaion.GPASettingsScreen
import com.ramcosta.composedestinations.annotation.Destination

@Destination<MoreNavGraph>(style = SlideTransitions::class)
@Composable
fun AppGPASettingsScreen() {
    GPASettingsScreen()
}