package com.hussienFahmy.myGpaManager.navigation.graphs

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class OnBoardingNavGraph(
    val start: Boolean = false
)