package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.semester_history_presentation.SemesterDetailRoot
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>(style = SlideTransitions::class)
@Composable
fun AppSemesterDetailScreen(
    semesterId: Long,
) {
    SemesterDetailRoot(
        semesterId = semesterId,
    )
}
