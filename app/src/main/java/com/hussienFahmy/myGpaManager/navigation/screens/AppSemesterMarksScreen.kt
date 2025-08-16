package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.semester_marks_presentaion.SemesterMarksScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>(style = SlideTransitions::class)
@Composable
fun AppSemesterMarksScreen() {
    SemesterMarksScreen()
}