package com.hussienFahmy.myGpaManager.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.hussienFahmy.myGpaManager.core.R
import com.ramcosta.composedestinations.generated.destinations.AppMoreScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppQuickScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSemesterMarksScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSemesterScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomNavDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Semester(
        direction = AppSemesterScreenDestination,
        icon = Icons.Outlined.DateRange,
        label = R.string.semester
    ),
    Marks(
        direction = AppSemesterMarksScreenDestination,
        icon = Icons.Outlined.Check,
        label = R.string.marks
    ),
    Quick(
        direction = AppQuickScreenDestination,
        icon = Icons.Outlined.Star,
        label = R.string.quick
    ),
    More(
        direction = AppMoreScreenDestination,
        icon = Icons.Outlined.MoreVert,
        label = R.string.more
    )
}