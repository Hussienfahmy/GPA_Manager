package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import com.hussienfahmy.gpa_system_sittings_presentaion.GPASettingsScreen
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@Destination<MoreNavGraph>(style = SlideTransitions::class)
@Composable
fun AppGPASettingsScreen() {
    GPASettingsScreen()
}