package com.hussienFahmy.myGpaManager.navigation

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class MoreNavGraph(
    val start: Boolean = false
)