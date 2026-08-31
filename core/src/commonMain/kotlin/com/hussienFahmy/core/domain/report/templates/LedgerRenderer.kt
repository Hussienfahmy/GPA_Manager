package com.hussienfahmy.core.domain.report.templates

import com.hussienfahmy.core.domain.report.AcademicReportData
import com.hussienfahmy.core.domain.report.ReportCommon
import com.hussienfahmy.core.domain.report.ReportTemplate
import com.hussienfahmy.core.domain.report.ReportTemplateRenderer
import com.hussienfahmy.core.domain.report.SubjectRow
import com.hussienfahmy.core.domain.report.escapeHtml
import com.hussienfahmy.core.domain.report.formatDate
import com.hussienfahmy.core.util.toFixedString

class LedgerRenderer : ReportTemplateRenderer {
    override val template = ReportTemplate.LEDGER

    override fun render(data: AcademicReportData): String {
        val html = StringBuilder()
        html.append(
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <title>Academic Report — ${data.studentName.escapeHtml()}</title>
            <style>
              @page {
                size: A4;
                margin: 14mm 12mm 14mm 12mm;
              }
              .page-frame {
                border: 0.6pt solid #14181f;
                padding: 10mm 10mm 8mm 10mm;
                box-sizing: border-box;
              }

              * { box-sizing: border-box; }

              html, body {
                margin: 0;
                padding: 0;
                background: #fdfcf8;
                color: #14181f;
                font-family: Helvetica, Arial, sans-serif;
                font-size: 9.5pt;
                line-height: 1.45;
                -webkit-print-color-adjust: exact;
                print-color-adjust: exact;
              }

              .serif { font-family: Georgia, 'Times New Roman', serif; }

              .report-header {
                display: table;
                width: 100%;
                border-bottom: 1.2pt solid #14181f;
                padding-bottom: 8pt;
                margin-bottom: 14pt;
              }
              .report-header .brand {
                display: table-cell;
                vertical-align: middle;
                width: 60%;
              }
              .report-header .meta {
                display: table-cell;
                vertical-align: middle;
                text-align: right;
                width: 40%;
                font-size: 8.5pt;
                color: #4a5160;
              }
              .brand-mark {
                display: inline-block;
                vertical-align: middle;
                width: 22pt;
                height: 22pt;
                margin-right: 6pt;
              }
              .brand-title {
                display: inline-block;
                vertical-align: middle;
              }
              .brand-title .app-name {
                font-family: Georgia, serif;
                font-size: 15pt;
                font-weight: bold;
                letter-spacing: 0.3pt;
                color: #1a2a4a;
                line-height: 1;
              }
              .brand-title .doc-kind {
                display: block;
                font-size: 7.5pt;
                letter-spacing: 2pt;
                text-transform: uppercase;
                color: #4a5160;
                margin-top: 3pt;
              }
              .meta .label {
                font-size: 7pt;
                letter-spacing: 1.5pt;
                text-transform: uppercase;
                color: #8a8f9a;
                display: block;
              }
              .meta .val {
                font-family: Georgia, serif;
                font-size: 10pt;
                color: #14181f;
              }

              .student-block {
                margin-bottom: 12pt;
              }
              .student-name {
                font-family: Georgia, serif;
                font-size: 20pt;
                font-weight: normal;
                color: #14181f;
                margin: 0 0 6pt 0;
                letter-spacing: 0.2pt;
              }

              .student-grid {
                display: table;
                width: 100%;
                border-collapse: collapse;
                margin-top: 4pt;
              }
              .student-grid .cell {
                display: table-cell;
                padding: 6pt 10pt 6pt 0;
                border-top: 0.5pt solid #d8dbe2;
                vertical-align: top;
                width: 25%;
              }
              .student-grid .cell + .cell { padding-left: 10pt; }
              .student-grid .k {
                font-size: 6.8pt;
                letter-spacing: 1.4pt;
                text-transform: uppercase;
                color: #8a8f9a;
                display: block;
                margin-bottom: 2pt;
              }
              .student-grid .v {
                font-size: 9.5pt;
                color: #14181f;
              }

              .summary {
                margin: 10pt 0 16pt 0;
                border: 0.6pt solid #14181f;
                display: table;
                width: 100%;
                border-collapse: collapse;
              }
              .summary .stat {
                display: table-cell;
                padding: 10pt 12pt;
                vertical-align: middle;
                width: 33.333%;
                border-right: 0.4pt solid #d8dbe2;
              }
              .summary .stat:last-child { border-right: 0; }
              .summary .stat .k {
                font-size: 6.8pt;
                letter-spacing: 1.4pt;
                text-transform: uppercase;
                color: #4a5160;
                display: block;
                margin-bottom: 4pt;
              }
              .summary .stat .v {
                font-family: Georgia, serif;
                font-size: 16pt;
                color: #1a2a4a;
                line-height: 1;
              }
              .summary .stat .v .suffix {
                font-size: 10pt;
                color: #8a8f9a;
                margin-left: 2pt;
              }
              .summary .stat .sub {
                font-size: 7.5pt;
                color: #8a8f9a;
                margin-top: 3pt;
                letter-spacing: 0.2pt;
              }

              .gpa-bar {
                height: 2.2pt;
                background: #e6e8ee;
                margin-top: 5pt;
                position: relative;
              }
              .gpa-bar .fill {
                position: absolute;
                left: 0; top: 0; bottom: 0;
                background: #1a2a4a;
              }

              .section-title {
                font-family: Georgia, serif;
                font-size: 11pt;
                font-weight: bold;
                color: #1a2a4a;
                margin: 0 0 6pt 0;
                padding-bottom: 3pt;
                border-bottom: 1pt solid #1a2a4a;
                letter-spacing: 0.3pt;
                text-transform: uppercase;
                page-break-after: avoid;
              }

              .semester {
                margin-bottom: 12pt;
                page-break-inside: avoid;
              }
              .semester-head {
                display: table;
                width: 100%;
                margin-bottom: 4pt;
                page-break-after: avoid;
              }
              .semester-head .label {
                display: table-cell;
                font-family: Georgia, serif;
                font-size: 10.5pt;
                font-weight: bold;
                color: #14181f;
                vertical-align: bottom;
                padding-bottom: 2pt;
              }
              .semester-head .meta {
                display: table-cell;
                text-align: right;
                font-size: 8.5pt;
                color: #4a5160;
                vertical-align: bottom;
                padding-bottom: 2pt;
              }
              .semester-head .meta .chip {
                display: inline-block;
                border: 0.4pt solid #c2c7d0;
                padding: 1.5pt 6pt;
                margin-left: 4pt;
                font-family: Helvetica, Arial, sans-serif;
                font-size: 8pt;
                color: #14181f;
                letter-spacing: 0.2pt;
              }
              .semester-head .meta .chip b {
                font-family: Georgia, serif;
                color: #1a2a4a;
              }

              .semester-placeholder {
                font-style: italic;
                color: #6a7080;
                font-size: 8.5pt;
                border-top: 0.4pt solid #d8dbe2;
                border-bottom: 0.4pt solid #d8dbe2;
                padding: 5pt 8pt;
                background: #f4f1ea;
              }

              table.subjects {
                width: 100%;
                border-collapse: collapse;
                font-size: 8.5pt;
                table-layout: fixed;
              }
              table.subjects col.c-subject  { width: auto; }
              table.subjects col.c-hrs      { width: 8%; }
              table.subjects col.c-grade    { width: 9%; }
              table.subjects col.c-pts      { width: 8%; }
              table.subjects col.c-mid      { width: 9%; }
              table.subjects col.c-prac     { width: 9%; }
              table.subjects col.c-oral     { width: 7%; }
              table.subjects col.c-proj     { width: 8%; }
              table.subjects col.c-final    { width: 8%; }
              table.subjects col.c-total    { width: 14%; }
              table.subjects thead th {
                text-align: left;
                font-weight: normal;
                font-size: 6.8pt;
                letter-spacing: 1.2pt;
                text-transform: uppercase;
                color: #4a5160;
                padding: 5pt 4pt;
                border-top: 0.8pt solid #14181f;
                border-bottom: 0.4pt solid #14181f;
                vertical-align: bottom;
              }
              table.subjects tbody td {
                padding: 4.5pt 4pt;
                border-bottom: 0.3pt solid #e6e8ee;
                vertical-align: top;
                color: #14181f;
                text-align: center;
              }
              table.subjects tbody td.subject-name { text-align: left; }
              table.subjects thead th { text-align: center; }
              table.subjects thead th:first-child { text-align: left; }
              table.subjects tbody tr:last-child td {
                border-bottom: 0.6pt solid #14181f;
              }
              table.subjects .num {
                text-align: center;
                font-variant-numeric: tabular-nums;
              }
              table.subjects .subject-name {
                font-weight: bold;
                font-size: 8.8pt;
                color: #14181f;
                word-wrap: break-word;
                overflow-wrap: break-word;
              }
              table.subjects .grade-pill {
                display: inline-block;
                font-family: Georgia, serif;
                font-weight: bold;
                font-size: 9pt;
                color: #1a2a4a;
                text-align: center;
              }
              table.subjects .muted {
                color: #b0b5be;
              }
              table.subjects tbody tr.no-grade td {
                color: #6a7080;
              }
              table.subjects tbody tr.no-grade .subject-name {
                color: #6a7080;
                font-style: italic;
              }

              ${
                ReportCommon.pageFooterCss(
                    accentHex = "#1a2a4a",
                    bgHex = "#fdfcf8",
                    borderHex = "#c2c7d0"
                )
            }

              .legend {
                font-size: 7pt;
                color: #8a8f9a;
                margin-top: 3pt;
                letter-spacing: 0.3pt;
              }
              .legend b { color: #4a5160; font-weight: normal; }

              .keep-alt { page-break-inside: avoid; }

              .brand-mark polygon { fill: #1a2a4a; }
              .brand-mark path,
              .brand-mark line,
              .brand-mark rect { stroke: #1a2a4a; }
              .brand-mark circle { fill: #1a2a4a; }
            </style>
            </head>
            <body>
            <div class="page-frame">

              <div class="report-header">
                <div class="brand">
                  ${ReportCommon.logoImg(data.logoBase64Png, sizePt = 26.0)}
                  <span class="brand-title">
                    <span class="app-name">GPA Manager</span>
                    <span class="doc-kind">Academic Record · Transcript</span>
                  </span>
                </div>
                <div class="meta">
                  <span class="label">Generated</span>
                  <span class="val">${formatDate()}</span>
                </div>
              </div>

              <div class="student-block">
                <h1 class="student-name">${data.studentName.escapeHtml()}</h1>
                <div class="student-grid">
                  <div class="cell">
                    <span class="k">University</span>
                    <span class="v">${data.university.escapeHtml()}</span>
                  </div>
                  <div class="cell">
                    <span class="k">Faculty</span>
                    <span class="v">${data.faculty.escapeHtml()}</span>
                  </div>
                  <div class="cell">
                    <span class="k">Department</span>
                    <span class="v">${data.department.escapeHtml()}</span>
                  </div>
                  <div class="cell">
                    <span class="k">Academic Level</span>
                    <span class="v">Level ${data.level}</span>
                  </div>
                </div>
              </div>

              <h2 class="section-title">Academic Summary</h2>
              <div class="summary">
                <div class="stat">
                  <span class="k">Cumulative GPA</span>
                  <span class="v">${data.cumulativeGPA.toFixedString(2)}<span class="suffix">/ ${data.maxGPA}</span></span>
                  <div class="gpa-bar"><div class="fill" style="width: ${
                ReportCommon.percentOfScale(
                    data.cumulativeGPA,
                    data.maxGPA
                )
            }%;"></div></div>
                  <div class="sub">${
                ReportCommon.percentOfScale(
                        data.cumulativeGPA,
                        data.maxGPA
                    ).toFixedString(1)
            }% of scale</div>
                </div>
                <div class="stat">
                  <span class="k">Credit Hours Earned</span>
                  <span class="v">${data.totalCreditHours}<span class="suffix">hrs</span></span>
                  <div class="sub">Across ${data.history.size} completed semesters</div>
                </div>
                <div class="stat">
                  <span class="k">Standing</span>
                  <span class="v" style="font-size:13pt;">${
                ReportCommon.resolveStanding(
                    data.cumulativeGPA,
                    data.maxGPA,
                    data.grades
                ).symbol
            }</span>
                  <div class="sub">${
                ReportCommon.resolveStanding(
                    data.cumulativeGPA,
                    data.maxGPA,
                    data.grades
                ).band
            }</div>
                </div>
              </div>
        """.trimIndent()
        )

        if (data.currentSemester != null) {
            val sem = data.currentSemester
            html.append(
                """
              <h2 class="section-title">Current Semester</h2>
              <div class="semester">
                <div class="semester-head">
                  <div class="label">Current Semester</div>
                  <div class="meta">
                    <span class="chip">GPA <b>${sem.semesterGPA.toFixedString(2)}</b></span>
                    <span class="chip">Credit Hrs <b>${sem.totalCreditHours}</b></span>
                  </div>
                </div>
                ${subjectTable(sem.subjects)}
                <div class="legend">
                  <b>Legend:</b> &nbsp;${ReportCommon.mdash()}&nbsp; indicates no value recorded &nbsp;·&nbsp; Grade / Points blank until final assessment
                </div>
              </div>
            """.trimIndent()
            )
        }

        if (data.history.isNotEmpty()) {
            html.append(
                """
              <div class="semester-history">
            """.trimIndent()
            )

            data.history.forEach { sem ->
                val gradeSym =
                    ReportCommon.gradeSymbolForGpa(sem.semesterGPA, data.maxGPA, data.grades)
                html.append(
                    """
                  <div class="semester">
                    <div class="semester-head">
                      <div class="label">${sem.label.escapeHtml()}</div>
                      <div class="meta">
                        <span class="chip">GPA <b>${sem.semesterGPA.toFixedString(2)}</b></span>
                        <span class="chip">Credit Hrs <b>${sem.totalCreditHours}</b></span>
                        <span class="chip">Grade <b>$gradeSym</b></span>
                      </div>
                    </div>
                """.trimIndent()
                )

                if (sem.subjects.isEmpty()) {
                    html.append("""<div class="semester-placeholder">Summary semester — no subject detail available.</div>""".trimIndent())
                } else {
                    html.append(subjectTable(sem.subjects))
                }

                html.append(
                    """
                  </div>
                """.trimIndent()
                )
            }

            html.append(
                """
              </div>
            """.trimIndent()
            )
        }

        html.append(
            """
            </div>
            ${ReportCommon.pageFooterHtml(data.logoBase64Png, data.qrBase64Png)}
            </body>
            </html>
        """.trimIndent()
        )

        return html.toString()
    }


    private fun subjectTable(subjects: List<SubjectRow>): String {
        val sb = StringBuilder()
        sb.append(
            """
            <table class="subjects">
              <colgroup>
                <col class="c-subject">
                <col class="c-hrs">
                <col class="c-grade">
                <col class="c-pts">
                <col class="c-mid">
                <col class="c-prac">
                <col class="c-oral">
                <col class="c-proj">
                <col class="c-final">
                <col class="c-total">
              </colgroup>
              <thead>
                <tr>
                  <th>Subject</th>
                  <th>Hrs</th>
                  <th>Grade</th>
                  <th>Pts</th>
                  <th>Mid</th>
                  <th>Prac</th>
                  <th>Oral</th>
                  <th>Proj</th>
                  <th>Final</th>
                  <th>Total</th>
                </tr>
              </thead>
              <tbody>
        """.trimIndent()
        )

        subjects.forEach { row ->
            val rowClass = if (row.gradeName == null) "no-grade" else ""
            sb.append("""<tr class="$rowClass">""")
            sb.append("""<td class="subject-name">${row.name.escapeHtml()}</td>""")
            sb.append("""<td class="num">${row.creditHours.toFixedString(1)}</td>""")
            sb.append("""<td>${if (row.gradeName != null) """<span class="grade-pill">${row.gradeName.escapeHtml()}</span>""" else """<span class="muted">${ReportCommon.mdash()}</span>"""}</td>""")
            sb.append("""<td class="num">${if (row.gradePoints != null) row.gradePoints.toFixedString(2) else """<span class="muted">${ReportCommon.mdash()}</span>"""}</td>""")
            sb.append("""<td class="num">${if (row.midterm != null) row.midterm.toFixedString(1) else """<span class="muted">${ReportCommon.mdash()}</span>"""}</td>""")
            sb.append("""<td class="num">${if (row.practical != null) row.practical.toFixedString(1) else """<span class="muted">${ReportCommon.mdash()}</span>"""}</td>""")
            sb.append("""<td class="num">${if (row.oral != null) row.oral.toFixedString(1) else """<span class="muted">${ReportCommon.mdash()}</span>"""}</td>""")
            sb.append("""<td class="num">${if (row.project != null) row.project.toFixedString(1) else """<span class="muted">${ReportCommon.mdash()}</span>"""}</td>""")
            sb.append("""<td class="num">${if (row.finalExam != null) row.finalExam.toFixedString(1) else """<span class="muted">${ReportCommon.mdash()}</span>"""}</td>""")

            val marks = ReportCommon.totalMarks(row)
            if (marks != null) {
                sb.append(
                    """<td class="num">${marks.first.toFixedString(1)}&nbsp;/&nbsp;${
                        marks.second.toFixedString(1)
                    }</td>"""
                )
            } else {
                sb.append("""<td class="num"><span class="muted">${ReportCommon.mdash()}</span></td>""")
            }

            sb.append("""</tr>""")
        }

        sb.append(
            """
              </tbody>
            </table>
        """.trimIndent()
        )

        return sb.toString()
    }
}
