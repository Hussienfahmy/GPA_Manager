package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hussienfahmy.core.domain.report.WebViewPdfPrinter
import com.hussienfahmy.myGpaManager.navigation.FadeTransitions
import com.hussienfahmy.semester_history_presentation.SemesterHistoryScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AppSemesterDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Destination<RootGraph>(style = FadeTransitions::class)
@Composable
fun AppSemesterHistoryScreen(
    snackBarHostState: SnackbarHostState,
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current

    SemesterHistoryScreen(
        snackBarHostState = snackBarHostState,
        onSemesterClick = { semesterId ->
            navigator.navigate(AppSemesterDetailScreenDestination(semesterId = semesterId))
        },
        onExportHtml = { html ->
            val title =
                "Academic_Report_${SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.US).format(Date())}"
            WebViewPdfPrinter.print(context = context, html = html, title = title)
        },
    )
}
