package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.theme.MeadowColors
import com.hussienfahmy.core_ui.theme.MeadowTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppToolbar(appNavigationState: AppNavigationState) {
    val currentRoute = appNavigationState.backStacks[appNavigationState.topLevelRoute]
        ?.lastOrNull() as? AppRoute
        ?: return
    if (currentRoute in appNavigationState.backStacks.keys) return

    val colors = MeadowTheme.colors

    PlatformToolbarContent(
        title = currentRoute.toolbarTitle(),
        accentColor = currentRoute.toolbarAccent(colors),
        backgroundColor = colors.paper,
        onBackClick = { appNavigationState.goBack() },
    )
}

// Android renders Material3's TopAppBar; iOS embeds a real native UINavigationBar via UIKitView.
@Composable
expect fun PlatformToolbarContent(
    title: String?,
    accentColor: Color,
    backgroundColor: Color,
    onBackClick: () -> Unit,
)

@Composable
private fun AppRoute.toolbarTitle(): String? = when (this) {
    is AppRoute.SemesterDetail -> semesterLabel
    AppRoute.GPASettings -> stringResource(Res.string.gpa_settings)
    AppRoute.GradeSettings -> stringResource(Res.string.grades_settings)
    AppRoute.SubjectSettings -> stringResource(Res.string.subject_settings)
    AppRoute.UserData -> stringResource(Res.string.personal_info_title)
    else -> null
}

private fun AppRoute.toolbarAccent(colors: MeadowColors) = when (this) {
    is AppRoute.SemesterDetail, AppRoute.SubjectSettings -> colors.history
    AppRoute.UserData -> colors.more
    AppRoute.GPASettings -> colors.semester
    AppRoute.GradeSettings -> colors.marks
    else -> colors.semester
}.deep
