package com.hussienfahmy.myGpaManager.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.hussienfahmy.core.R
import com.ramcosta.composedestinations.generated.destinations.AppMoreScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppQuickScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSemesterHistoryScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSemesterMarksScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSemesterScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomNavDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @param:StringRes val label: Int
) {
    Semester(
        direction = AppSemesterScreenDestination,
        icon = Icons.Outlined.CalendarMonth,
        label = R.string.semester
    ),
    Marks(
        direction = AppSemesterMarksScreenDestination,
        icon = Icons.Outlined.Check,
        label = R.string.marks
    ),
    History(
        direction = AppSemesterHistoryScreenDestination,
        icon = Icons.Outlined.History,
        label = R.string.history
    ),
    Quick(
        direction = AppQuickScreenDestination,
        icon = Icons.Outlined.AutoAwesome,
        label = R.string.quick
    ),
    More(
        direction = AppMoreScreenDestination,
        icon = Icons.Outlined.MoreHoriz,
        label = R.string.more
    )
}