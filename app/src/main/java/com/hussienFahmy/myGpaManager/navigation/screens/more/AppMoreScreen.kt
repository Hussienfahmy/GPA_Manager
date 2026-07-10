package com.hussienfahmy.myGpaManager.navigation.screens.more

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.util.truncate
import com.hussienfahmy.core_ui.presentation.analytics.TrackScreenTime
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowConfirmationSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowRowDivider
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowSettingsGroup
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowSettingsRow
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowUserCard
import com.hussienfahmy.core_ui.presentation.components.meadow.SettingsGroupLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.SettingsRowTrailing
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.myGpaManager.navigation.FadeTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination<MoreNavGraph>(start = true, style = FadeTransitions::class)
@Composable
fun AppMoreScreen(
    modifier: Modifier = Modifier,
    moreViewModel: MoreViewModel = koinViewModel(),
    onUserDataCardClick: () -> Unit,
    onGPASettingsClick: () -> Unit,
    onGradeSettingsClick: () -> Unit,
    onSubjectSettingsClick: () -> Unit,
) {
    TrackScreenTime(AnalyticsValues.SCREEN_MORE)

    val userData by moreViewModel.userData.collectAsStateWithLifecycle(null)

    if (userData != null && !moreViewModel.isSigningOut) {
        MoreScreenContent(
            modifier = modifier,
            userData = userData!!,
            onUserDataCardClick = onUserDataCardClick,
            onGPASettingsClick = onGPASettingsClick,
            onGradeSettingsClick = onGradeSettingsClick,
            onSubjectSettingsClick = onSubjectSettingsClick,
            onSignOutClick = { moreViewModel.signOut() },
            onAppRatingClick = { moreViewModel.logAppRatingClicked() },
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
    onAppRatingClick: () -> Unit,
) {
    val colors = MeadowTheme.colors
    val context = LocalContext.current
    var showSignOutSheet by remember { mutableStateOf(false) }
    val githubRepoUrl = stringResource(R.string.github_repo_url)
    val playStoreLink = stringResource(R.string.play_store_link)

    MeadowAccentProvider(colors.more) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MeadowUserCard(
                name = userData.name,
                institutionLine = stringResource(
                    R.string.more_institution_line,
                    userData.academicInfo.university,
                    userData.academicInfo.faculty,
                    userData.academicInfo.department,
                ),
                gpaChip = stringResource(
                    R.string.more_gpa_chip,
                    userData.academicProgress.cumulativeGPA.truncate(),
                ),
                yearChip = stringResource(
                    R.string.level_semester,
                    userData.academicInfo.level,
                    when (userData.academicInfo.semester) {
                        UserData.AcademicInfo.Semester.First -> stringResource(R.string.first)
                        UserData.AcademicInfo.Semester.Second -> stringResource(R.string.second)
                    },
                ),
                photoUrl = userData.photoUrl,
                onClick = onUserDataCardClick,
            )

            SettingsGroupLabel(text = stringResource(R.string.more_group_settings))
            MeadowSettingsGroup {
                MeadowSettingsRow(
                    icon = Icons.Outlined.DonutLarge,
                    title = stringResource(R.string.gpa_settings),
                    summary = stringResource(R.string.gpa_settings_details),
                    tileAccent = colors.semester,
                    onClick = onGPASettingsClick,
                )
                MeadowRowDivider()
                MeadowSettingsRow(
                    icon = Icons.Outlined.Star,
                    title = stringResource(R.string.grades_settings),
                    summary = stringResource(R.string.grades_settings_screen_summary),
                    tileAccent = colors.marks,
                    onClick = onGradeSettingsClick,
                )
                MeadowRowDivider()
                MeadowSettingsRow(
                    icon = Icons.Outlined.GridView,
                    title = stringResource(R.string.subject_settings),
                    summary = stringResource(R.string.subject_settings_screen_summary),
                    tileAccent = colors.history,
                    onClick = onSubjectSettingsClick,
                )
            }

            SettingsGroupLabel(text = stringResource(R.string.more_group_app))
            MeadowSettingsGroup {
                MeadowSettingsRow(
                    icon = Icons.Outlined.Code,
                    title = stringResource(R.string.contribute_to_app),
                    summary = stringResource(R.string.contribute_to_app_summary),
                    trailing = SettingsRowTrailing.External,
                    onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, githubRepoUrl.toUri()))
                    },
                )
                MeadowRowDivider()
                MeadowSettingsRow(
                    icon = Icons.Outlined.WorkspacePremium,
                    title = stringResource(R.string.is_app_useful),
                    summary = stringResource(R.string.is_app_useful_details),
                    tileAccent = colors.marks,
                    trailing = SettingsRowTrailing.External,
                    onClick = {
                        onAppRatingClick()
                        context.startActivity(Intent(Intent.ACTION_VIEW, playStoreLink.toUri()))
                    },
                )
            }

            MeadowSettingsGroup {
                MeadowSettingsRow(
                    icon = Icons.AutoMirrored.Outlined.Logout,
                    title = stringResource(R.string.sign_out),
                    trailing = SettingsRowTrailing.None,
                    danger = true,
                    onClick = { showSignOutSheet = true },
                )
            }
        }
    }

    if (showSignOutSheet) {
        MeadowConfirmationSheet(
            title = stringResource(R.string.sign_out),
            body = stringResource(R.string.sign_out_confirmation_message),
            confirmText = stringResource(R.string.sign_out),
            onConfirm = onSignOutClick,
            onDismiss = { showSignOutSheet = false },
        )
    }
}
