package com.hussienFahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.myGpaManager.navigation.SlideTransitions
import com.hussienFahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.hussienfahmy.gpa_system_sittings_presentaion.GPASettingsScreen
import com.hussienfahmy.subject_settings_presentation.SubjectsSettingsScreen
import com.ramcosta.composedestinations.annotation.Destination

@Destination<OnBoardingNavGraph>(style = SlideTransitions::class)
@Composable
fun AppOnBoardingGPASubjectsSettings(
    onStartClick: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val spacing = LocalSpacing.current

    Column {
        Text(
            text = "Please Review Your GPA & Subjects Settings according to your University",
            modifier = Modifier
                .padding(top = spacing.medium)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(spacing.medium))

        GPASettingsScreen()

        SubjectsSettingsScreen(snackBarHostState = snackBarHostState)

        OutlinedButton(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing.medium)
        ) {
            Text(text = "Start")
        }
    }
}