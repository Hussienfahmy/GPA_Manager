package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import com.hussienfahmy.semester_history_presentation.SemesterDetailRoot

@Composable
fun AppSemesterDetailScreen(
    semesterId: Long,
) {
    SemesterDetailRoot(
        semesterId = semesterId,
    )
}
