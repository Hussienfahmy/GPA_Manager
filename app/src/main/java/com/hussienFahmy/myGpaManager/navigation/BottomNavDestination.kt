package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.hussienfahmy.core.generated.resources.Res
import com.ramcosta.composedestinations.generated.destinations.AppMoreScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppQuickScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSemesterHistoryScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSemesterMarksScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSemesterScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import org.jetbrains.compose.resources.StringResource

enum class BottomNavDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    val label: StringResource
) {
    Semester(
        direction = AppSemesterScreenDestination,
        icon = Icons.Outlined.CalendarMonth,
        label = Res.string.semester
    ),
    Marks(
        direction = AppSemesterMarksScreenDestination,
        icon = Icons.Outlined.Check,
        label = Res.string.marks
    ),
    History(
        direction = AppSemesterHistoryScreenDestination,
        icon = Icons.Outlined.History,
        label = Res.string.history
    ),
    Quick(
        direction = AppQuickScreenDestination,
        icon = Icons.Outlined.AutoAwesome,
        label = Res.string.quick
    ),
    More(
        direction = AppMoreScreenDestination,
        icon = Icons.Outlined.MoreHoriz,
        label = Res.string.more
    )
}