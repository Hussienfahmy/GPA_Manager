package com.hussienFahmy.myGpaManager.navigation.screens.more

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hussienFahmy.core.domain.user_data.model.UserData
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.core_ui.presentation.user_data.components.UserInfoCard
import com.hussienFahmy.myGpaManager.core.R
import com.hussienFahmy.myGpaManager.navigation.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@MoreNavGraph(start = true)
@Destination
@Composable
fun AppMoreScreen(
    modifier: Modifier = Modifier,
    moreViewModel: MoreViewModel = hiltViewModel(),
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
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

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
            onClick = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.play_store_link))
                    )
                )
            },
            title = stringResource(R.string.is_app_useful),
            summary = stringResource(R.string.is_app_useful_details)
        )
    }
}
