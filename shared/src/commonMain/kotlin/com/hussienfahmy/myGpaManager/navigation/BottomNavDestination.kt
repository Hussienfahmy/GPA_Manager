package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.hussienfahmy.core.generated.resources.Res
import org.jetbrains.compose.resources.StringResource

enum class BottomNavDestination(
    val route: AppRoute,
    val icon: ImageVector,
    val label: StringResource
) {
    Semester(
        route = AppRoute.Semester,
        icon = Icons.Outlined.CalendarMonth,
        label = Res.string.semester
    ),
    Marks(
        route = AppRoute.SemesterMarks,
        icon = Icons.Outlined.Check,
        label = Res.string.marks
    ),
    History(
        route = AppRoute.SemesterHistory,
        icon = Icons.Outlined.History,
        label = Res.string.history
    ),
    Quick(
        route = AppRoute.Quick,
        icon = Icons.Outlined.AutoAwesome,
        label = Res.string.quick
    ),
    More(
        route = AppRoute.More,
        icon = Icons.Outlined.MoreHoriz,
        label = Res.string.more
    )
}
