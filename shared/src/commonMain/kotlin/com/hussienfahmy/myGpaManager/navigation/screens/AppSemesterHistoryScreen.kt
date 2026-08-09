package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.core.domain.report.PdfReportPrinter
import com.hussienfahmy.semester_history_presentation.SemesterHistoryScreen
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// java.text.SimpleDateFormat/java.util.Date have no Kotlin/Native equivalent - rewritten on
// kotlin.time.Clock/kotlinx-datetime, matching the pattern already used in ReportCommon.kt.
@OptIn(ExperimentalTime::class)
private fun exportTimestamp(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    fun Int.pad() = toString().padStart(2, '0')
    return "${now.year}-${now.month.number.pad()}-${now.day.pad()}_" +
        "${now.hour.pad()}-${now.minute.pad()}"
}

@Composable
fun AppSemesterHistoryScreen(
    snackBarHostState: SnackbarHostState,
    onSemesterClick: (Long) -> Unit,
) {
    val pdfReportPrinter = koinInject<PdfReportPrinter>()

    SemesterHistoryScreen(
        snackBarHostState = snackBarHostState,
        onSemesterClick = onSemesterClick,
        onExportHtml = { html ->
            val title = "Academic_Report_${exportTimestamp()}"
            pdfReportPrinter.print(html = html, title = title)
        },
    )
}
