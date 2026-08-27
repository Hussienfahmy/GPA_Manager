package com.hussienfahmy.myGpaManager.navigation.screens.more

import androidx.compose.animation.Crossfade
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
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.util.UrlOpener
import com.hussienfahmy.core.util.storeDisplayNameRes
import com.hussienfahmy.core.util.storeRatingUrl
import com.hussienfahmy.core.util.truncate
import com.hussienfahmy.core_ui.LocalScaffoldContentPadding
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
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

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

    val currentUserData = userData
    val loading = currentUserData == null || moreViewModel.isSigningOut
    Crossfade(targetState = loading, label = "moreScreenLoading") { isLoading ->
        if (isLoading || currentUserData == null) {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            MoreScreenContent(
                modifier = modifier,
                userData = currentUserData,
                onUserDataCardClick = onUserDataCardClick,
                onGPASettingsClick = onGPASettingsClick,
                onGradeSettingsClick = onGradeSettingsClick,
                onSubjectSettingsClick = onSubjectSettingsClick,
                onSignOutClick = { moreViewModel.signOut() },
                onAppRatingClick = { moreViewModel.logAppRatingClicked() },
            )
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
    val urlOpener = koinInject<UrlOpener>()
    var showSignOutSheet by remember { mutableStateOf(false) }
    val githubRepoUrl = stringResource(Res.string.github_repo_url)
    val ratingUrl = storeRatingUrl()
    val scaffoldPadding = LocalScaffoldContentPadding.current

    MeadowAccentProvider(colors.more) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp,
                )
                .padding(bottom = scaffoldPadding.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MeadowUserCard(
                name = userData.name,
                institutionLine = stringResource(
                    Res.string.more_institution_line,
                    userData.academicInfo.university,
                    userData.academicInfo.faculty,
                    userData.academicInfo.department,
                ),
                gpaChip = stringResource(
                    Res.string.more_gpa_chip,
                    userData.academicProgress.cumulativeGPA.truncate(),
                ),
                yearChip = stringResource(
                    Res.string.level_semester,
                    userData.academicInfo.level,
                    when (userData.academicInfo.semester) {
                        UserData.AcademicInfo.Semester.First -> stringResource(Res.string.first)
                        UserData.AcademicInfo.Semester.Second -> stringResource(Res.string.second)
                    },
                ),
                photoUrl = userData.photoUrl,
                onClick = onUserDataCardClick,
            )

            SettingsGroupLabel(text = stringResource(Res.string.more_group_settings))
            MeadowSettingsGroup {
                MeadowSettingsRow(
                    icon = Icons.Outlined.DonutLarge,
                    title = stringResource(Res.string.gpa_settings),
                    summary = stringResource(Res.string.gpa_settings_details),
                    tileAccent = colors.semester,
                    onClick = onGPASettingsClick,
                )
                MeadowRowDivider()
                MeadowSettingsRow(
                    icon = Icons.Outlined.Star,
                    title = stringResource(Res.string.grades_settings),
                    summary = stringResource(Res.string.grades_settings_screen_summary),
                    tileAccent = colors.marks,
                    onClick = onGradeSettingsClick,
                )
                MeadowRowDivider()
                MeadowSettingsRow(
                    icon = Icons.Outlined.GridView,
                    title = stringResource(Res.string.subject_settings),
                    summary = stringResource(Res.string.subject_settings_screen_summary),
                    tileAccent = colors.history,
                    onClick = onSubjectSettingsClick,
                )
            }

            SettingsGroupLabel(text = stringResource(Res.string.more_group_app))
            MeadowSettingsGroup {
                MeadowSettingsRow(
                    icon = Icons.Outlined.Code,
                    title = stringResource(Res.string.contribute_to_app),
                    summary = stringResource(Res.string.contribute_to_app_summary),
                    trailing = SettingsRowTrailing.External,
                    onClick = { urlOpener.open(githubRepoUrl) },
                )
                MeadowRowDivider()
                MeadowSettingsRow(
                    icon = Icons.Outlined.WorkspacePremium,
                    title = stringResource(Res.string.is_app_useful),
                    summary = stringResource(Res.string.is_app_useful_details, stringResource(storeDisplayNameRes)),
                    tileAccent = colors.marks,
                    trailing = SettingsRowTrailing.External,
                    onClick = {
                        onAppRatingClick()
                        ratingUrl?.let { urlOpener.open(it) }
                    },
                )
            }

            MeadowSettingsGroup {
                MeadowSettingsRow(
                    icon = Icons.AutoMirrored.Outlined.Logout,
                    title = stringResource(Res.string.sign_out),
                    trailing = SettingsRowTrailing.None,
                    danger = true,
                    onClick = { showSignOutSheet = true },
                )
            }
        }
    }

    if (showSignOutSheet) {
        MeadowConfirmationSheet(
            title = stringResource(Res.string.sign_out),
            body = stringResource(Res.string.sign_out_confirmation_message),
            confirmText = stringResource(Res.string.sign_out),
            onConfirm = onSignOutClick,
            onDismiss = { showSignOutSheet = false },
        )
    }
}
