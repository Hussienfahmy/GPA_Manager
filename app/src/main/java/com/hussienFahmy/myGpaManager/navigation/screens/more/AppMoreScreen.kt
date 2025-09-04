package com.hussienfahmy.myGpaManager.navigation.screens.more

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.user_data.components.UserInfoCard
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination<MoreNavGraph>(start = true, style = SlideTransitions::class)
@Composable
fun AppMoreScreen(
    modifier: Modifier = Modifier,
    moreViewModel: MoreViewModel = koinViewModel(),
    onUserDataCardClick: () -> Unit,
    onGPASettingsClick: () -> Unit,
    onGradeSettingsClick: () -> Unit,
    onSubjectSettingsClick: () -> Unit,
) {
    val userData = moreViewModel.userData

    if (userData != null) {
        MoreScreenContent(
            modifier = modifier,
            userData = userData,
            onUserDataCardClick = onUserDataCardClick,
            onGPASettingsClick = onGPASettingsClick,
            onGradeSettingsClick = onGradeSettingsClick,
            onSubjectSettingsClick = onSubjectSettingsClick,
            onSignOutClick = { moreViewModel.signOut() },
            isSigningOut = moreViewModel.isSigningOut,
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun MoreScreenContent(
    modifier: Modifier = Modifier,
    userData: UserData,
    onUserDataCardClick: () -> Unit,
    onGPASettingsClick: () -> Unit,
    onGradeSettingsClick: () -> Unit,
    onSubjectSettingsClick: () -> Unit,
    onSignOutClick: () -> Unit,
    isSigningOut: Boolean,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    var showSignOutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        UserInfoCard(onCardClick = onUserDataCardClick, userData = userData)

        MoreItem(
            onClick = onGPASettingsClick,
            title = stringResource(id = R.string.gpa_settings),
            summary = stringResource(id = R.string.gpa_settings_details)
        )

        MoreItem(
            onClick = onGradeSettingsClick,
            title = stringResource(R.string.grades_settings),
            summary = stringResource(R.string.grades_settings_screen_summary)
        )

        MoreItem(
            onClick = onSubjectSettingsClick,
            title = stringResource(R.string.subject_settings),
            summary = stringResource(R.string.subject_settings_screen_summary)
        )

        MoreItem(
            onClick = { if (!isSigningOut) showSignOutDialog = true },
            title = stringResource(R.string.sign_out),
            summary = stringResource(R.string.sign_out_details)
        )

        MoreItem(
            onClick = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        context.getString(R.string.play_store_link).toUri()
                    )
                )
            },
            title = stringResource(R.string.is_app_useful),
            summary = stringResource(R.string.is_app_useful_details)
        )
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { if (!isSigningOut) showSignOutDialog = false },
            title = { Text(text = stringResource(id = R.string.are_you_sure)) },
            text = { Text(text = stringResource(id = R.string.sign_out_confirmation_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSignOutClick()
                        showSignOutDialog = false
                    }
                ) { Text(text = stringResource(id = R.string.ok)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSignOutDialog = false },
                    enabled = !isSigningOut
                ) { Text(text = stringResource(id = R.string.cancel)) }
            }
        )
    }
}
