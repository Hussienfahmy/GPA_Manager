package com.hussienFahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.grades_setting_presentation.GradeSettingsScreen
import com.hussienFahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@OnBoardingNavGraph
@Destination
@Composable
fun AppOnBoardingGradesSettingsScreen(
    onStartClick: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
    ) {
        Spacer(modifier = Modifier.height(spacing.medium))

        OutlinedButton(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing.medium)
        ) {
            Text(text = "Start")
        }

        Spacer(modifier = Modifier.height(spacing.medium))

        Text(
            text = "Please review your grades settings carefully to get correct results\nyou may check your academic institution website for more details",
            modifier = Modifier
                .padding(top = spacing.medium)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(spacing.medium))

        GradeSettingsScreen(snackBarHostState = snackBarHostState)
    }
}