package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.core.domain.report.createPdfReportPrinter
import com.hussienfahmy.semester_history_presentation.SemesterHistoryScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppSemesterHistoryScreen(
    snackBarHostState: SnackbarHostState,
    onSemesterClick: (Long) -> Unit,
) {
    SemesterHistoryScreen(
        snackBarHostState = snackBarHostState,
        onSemesterClick = onSemesterClick,
        onExportHtml = { html ->
            val title =
                "Academic_Report_${SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.US).format(Date())}"
            createPdfReportPrinter().print(html = html, title = title)
        },
    )
}
