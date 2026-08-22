package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.theme.MeadowColors
import com.hussienfahmy.core_ui.theme.MeadowTheme
import org.jetbrains.compose.resources.stringResource

// Gives a pushed sub-screen its own toolbar instead of one shared toolbar in the root Scaffold -
// keeps the appear/disappear animation local to that screen. Insets zeroed since root already
// reserves the status bar for every route.
@Composable
fun ScreenWithToolbar(
    route: AppRoute,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val colors = MeadowTheme.colors

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            PlatformToolbarContent(
                title = route.toolbarTitle(),
                accentColor = route.toolbarAccent(colors),
                backgroundColor = colors.paper,
                onBackClick = onBackClick,
            )
        },
        content = content,
    )
}

// Android renders Material3's TopAppBar; iOS embeds a native UINavigationBar via UIKitView.
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
