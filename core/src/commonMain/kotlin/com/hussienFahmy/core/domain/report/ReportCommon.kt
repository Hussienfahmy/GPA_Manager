package com.hussienfahmy.core.domain.report

import com.hussienfahmy.core.domain.report.ReportCommon.pageFooterCss
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal fun String?.escapeHtml(): String {
    if (this == null) return ""
    return this
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;")
}

internal object ReportCommon {
    data class Standing(val symbol: String, val band: String)

    private const val DISCLAIMER_TEXT =
        "This document is generated from self-reported data and does not constitute an official academic transcript. For verified records, request a transcript from your institution."

    fun mdash(): String = "—"

    fun gradeSymbolForGpa(gpa: Double, maxGpa: Double, grades: List<GradeThreshold>): String =
        resolveStanding(gpa, maxGpa, grades).symbol

    fun logoImg(base64Png: String?, sizePt: Double = 18.0): String {
        if (base64Png.isNullOrEmpty()) return ""
        return """<img src="data:image/png;base64,$base64Png" alt="GPA Manager" style="width:${sizePt}pt;height:${sizePt}pt;display:inline-block;vertical-align:middle;border-radius:3pt;"/>"""
    }

    fun qrImg(base64Png: String?, sizePt: Double = 34.0): String {
        if (base64Png.isNullOrEmpty()) return ""
        return """<img src="data:image/png;base64,$base64Png" alt="QR" style="width:${sizePt}pt;height:${sizePt}pt;display:inline-block;vertical-align:middle;"/>"""
    }

    /**
     * Fixed page footer (repeats on every printed page in Chromium WebView print).
     * Caller must include CSS via [pageFooterCss] and reserve body padding-bottom.
     */
    fun pageFooterHtml(logoBase64Png: String?, qrBase64Png: String?): String {
        val qr = qrImg(qrBase64Png, sizePt = 34.0)
        val logo = logoImg(logoBase64Png, sizePt = 16.0)
        return """
        <div class="report-footer">
          <div class="rf-brand">$logo<span class="rf-app">GPA Manager</span></div>
          <div class="rf-disc">$DISCLAIMER_TEXT</div>
          <div class="rf-qr"><span class="rf-qr-cap">Get the app</span><span class="rf-qr-img">$qr</span></div>
        </div>
        """.trimIndent()
    }

    fun pageFooterCss(
        accentHex: String = "#14181f",
        bgHex: String = "#ffffff",
        borderHex: String = "#d4d4d2"
    ): String = """
      body { padding-bottom: 22mm; }
      .report-footer {
        position: fixed;
        bottom: 0; left: 0; right: 0;
        padding: 3mm 10mm;
        background: $bgHex;
        border-top: 0.5pt solid $borderHex;
        display: table;
        width: 100%;
        box-sizing: border-box;
        font-family: Helvetica, Arial, sans-serif;
      }
      .report-footer .rf-brand {
        display: table-cell;
        vertical-align: middle;
        width: 26%;
        font-size: 8pt;
        color: $accentHex;
      }
      .report-footer .rf-brand .rf-app {
        font-family: Georgia, 'Times New Roman', serif;
        font-weight: bold;
        margin-left: 5pt;
        vertical-align: middle;
        letter-spacing: 0.3pt;
      }
      .report-footer .rf-disc {
        display: table-cell;
        vertical-align: middle;
        font-size: 6.8pt;
        color: #5b5f70;
        font-style: italic;
        line-height: 1.4;
        padding: 0 8pt;
        width: 50%;
      }
      .report-footer .rf-qr {
        display: table-cell;
        vertical-align: middle;
        text-align: right;
        width: 24%;
      }
      .report-footer .rf-qr-img { display: inline-block; vertical-align: middle; margin-left: 14pt; }
      .report-footer .rf-qr-cap {
        display: inline-block;
        vertical-align: middle;
        font-size: 6.5pt;
        letter-spacing: 1.5pt;
        text-transform: uppercase;
        color: #8a8d99;
      }
    """.trimIndent()

    fun resolveStanding(gpa: Double, maxGpa: Double, grades: List<GradeThreshold>): Standing {
        if (maxGpa == 0.0) return Standing("—", "No Data")
        val percentage = (gpa / maxGpa) * 100
        val grade = grades.firstOrNull { it.percentage <= percentage }
        return Standing(
            symbol = grade?.symbol ?: "—",
            band = when {
                percentage >= 90 -> "Excellent"
                percentage >= 80 -> "Very Good"
                percentage >= 70 -> "Good"
                percentage >= 60 -> "Satisfactory"
                else -> "Needs Improvement"
            }
        )
    }

    fun gradePillClass(gradeName: String?): String {
        if (gradeName == null) return "grade-unassigned"
        return when {
            gradeName.contains("A", ignoreCase = true) -> "grade-a"
            gradeName.contains("B", ignoreCase = true) -> "grade-b"
            gradeName.contains("C", ignoreCase = true) -> "grade-c"
            gradeName.contains("D", ignoreCase = true) -> "grade-d"
            gradeName.contains("F", ignoreCase = true) -> "grade-f"
            else -> "grade-unassigned"
        }
    }

    fun totalMarks(row: SubjectRow): Pair<Double, Double>? {
        val hasAny = row.midterm != null || row.practical != null ||
                row.oral != null || row.project != null || row.finalExam != null
        if (!hasAny) return null
        val earned = (row.midterm ?: 0.0) + (row.practical ?: 0.0) +
                (row.oral ?: 0.0) + (row.project ?: 0.0) + (row.finalExam ?: 0.0)
        return earned to row.courseTotalMarks
    }

    fun percentOfScale(marks: Double, scale: Double): Double {
        if (scale == 0.0) return 0.0
        return (marks / scale) * 100
    }
}

// java.text.SimpleDateFormat/java.util.Date have no Kotlin/Native equivalent. Every call site
// uses the default (today's date, in the device's local timezone) - none pass an explicit date -
// so this is rewritten around kotlin.time.Clock/kotlinx.datetime rather than kept JVM-only.
// Locale.US was already hardcoded (not the device locale), so a fixed English month-abbreviation
// table is equivalent, not a behavior change.
private val MONTH_ABBREVIATIONS = arrayOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

@OptIn(ExperimentalTime::class)
internal fun formatDate(instant: Instant? = Clock.System.now()): String {
    if (instant == null) return "—"
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${MONTH_ABBREVIATIONS[date.month.number - 1]} ${date.dayOfMonth}, ${date.year}"
}

@OptIn(ExperimentalTime::class)
internal fun formatShortDate(instant: Instant? = Clock.System.now()): String {
    if (instant == null) return "—"
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${MONTH_ABBREVIATIONS[date.month.number - 1]} ${date.dayOfMonth}"
}
