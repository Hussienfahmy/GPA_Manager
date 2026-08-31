package com.hussienfahmy.core.domain.report.templates

import com.hussienfahmy.core.domain.report.AcademicReportData
import com.hussienfahmy.core.domain.report.ReportCommon
import com.hussienfahmy.core.domain.report.ReportTemplate
import com.hussienfahmy.core.domain.report.ReportTemplateRenderer
import com.hussienfahmy.core.domain.report.escapeHtml
import com.hussienfahmy.core.domain.report.formatDate
import com.hussienfahmy.core.util.toFixedString

class TimelineRenderer : ReportTemplateRenderer {
    override val template = ReportTemplate.TIMELINE

    override fun render(data: AcademicReportData): String = """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Academic Timeline — ${data.studentName.escapeHtml()}</title>
<style>
@page {size:A4;margin:14mm 0 0 0}
*{box-sizing:border-box}
html,body{margin:0;padding:0;background:#f7f7f5;color:#111214;font-family:Helvetica,Arial,sans-serif;font-size:9pt;line-height:1.5;-webkit-print-color-adjust:exact;print-color-adjust:exact}
.page-frame{padding:0 12mm;background:#f7f7f5;min-height:100vh}

.hdr{padding-bottom:12pt;margin-bottom:18pt;border-bottom:2pt solid #111214;display:table;width:100%}
.hdr .l{display:table-cell;vertical-align:bottom}
.hdr .r{display:table-cell;text-align:right;vertical-align:bottom;font-size:7pt;letter-spacing:1.5pt;text-transform:uppercase;color:#8a8c90}
.kicker{font-size:7pt;letter-spacing:3pt;text-transform:uppercase;color:#ec4899;margin-bottom:4pt;font-weight:bold}
.title{font-family:Georgia,serif;font-size:28pt;line-height:1.1;font-weight:normal;margin:0;letter-spacing:-0.3pt}
.subinfo{color:#8a8c90;font-size:8.5pt;margin-top:6pt}
.subinfo b{color:#111214;font-weight:normal}

.strip{display:table;width:100%;margin-bottom:18pt;border-collapse:collapse}
.strip .cell{display:table-cell;padding:8pt 14pt;border-right:0.5pt solid #d4d4d2;vertical-align:top;width:25%}
.strip .cell:last-child{border-right:0}
.strip .k{font-size:6.5pt;letter-spacing:1.5pt;text-transform:uppercase;color:#8a8c90;margin-bottom:3pt}
.strip .v{font-family:Georgia,serif;font-size:18pt;line-height:1;color:#111214}
.strip .v .suf{font-size:9pt;color:#8a8c90;margin-left:2pt}

.journey{margin-bottom:20pt;padding:12pt 14pt;background:#fff;border:0.4pt solid #e5e5e3;border-radius:3pt}
.journey .t{font-family:Georgia,serif;font-size:10pt;font-weight:bold;margin:0 0 2pt 0}
.journey svg{width:100%;height:50pt;display:block}

.timeline{position:relative;padding-left:26pt;margin-top:8pt}
.timeline::before{content:'';position:absolute;left:8pt;top:4pt;bottom:4pt;width:1.2pt;background:#111214}
.node{position:relative;margin-bottom:14pt;page-break-inside:avoid}
.node::before{content:'';position:absolute;left:-22.5pt;top:4pt;width:9pt;height:9pt;border-radius:50%;background:#fff;border:1.8pt solid #111214}
.node.now::before{background:#ec4899;border-color:#ec4899}
.node .when{font-size:6.8pt;letter-spacing:1.5pt;text-transform:uppercase;color:#8a8c90;margin-bottom:3pt}
.node .head{display:table;width:100%;margin-bottom:6pt}
.node .head .ttl{display:table-cell;font-family:Georgia,serif;font-size:13pt;font-weight:bold;vertical-align:middle}
.node .head .badge{display:inline-block;font-family:Helvetica,sans-serif;font-size:6.5pt;letter-spacing:1.2pt;padding:2pt 6pt;background:#ec4899;color:#fff;text-transform:uppercase;border-radius:2pt;margin-left:6pt;vertical-align:middle}
.node .head .stats{display:table-cell;text-align:right;vertical-align:middle;font-size:8pt}
.node .head .stats .stat{display:inline-block;margin-left:10pt}
.node .head .stats .stat .l{color:#8a8c90;font-size:6.5pt;letter-spacing:1.2pt;text-transform:uppercase;margin-right:3pt}
.node .head .stats .stat .v{font-family:Georgia,serif;font-weight:bold;font-size:11pt;color:#111214}

table.subjects{width:100%;border-collapse:collapse;font-size:8.2pt;table-layout:fixed;margin-top:2pt}
table.subjects thead th{text-align:center;font-weight:normal;font-size:6.2pt;letter-spacing:1.3pt;text-transform:uppercase;color:#8a8c90;padding:5pt 3pt;border-bottom:0.3pt solid #111214}
table.subjects thead th:first-child{text-align:left}
table.subjects tbody td{text-align:center;padding:5pt 3pt;border-bottom:0.3pt solid #ececea;font-variant-numeric:tabular-nums;vertical-align:middle}
table.subjects tbody tr:last-child td{border-bottom:0}
table.subjects tbody td.subject-name{text-align:left;font-weight:bold;font-size:8.6pt;word-wrap:break-word}
table.subjects tbody tr.no-grade td.subject-name{color:#b6b8bc;font-style:italic;font-weight:normal}
table.subjects .muted{color:#d4d4d2}
.grade-pill{display:inline-block;min-width:22pt;padding:1.5pt 5pt;border-radius:2pt;font-family:Georgia,serif;font-weight:bold;font-size:9pt;background:#f3f4f6;color:#111214;border:0.4pt solid #e5e5e3}
.grade-pill.gA{background:#111214;color:#fff;border-color:#111214}
.grade-pill.gB{background:#ec4899;color:#fff;border-color:#ec4899}

.placeholder{padding:8pt 12pt;background:#fff;border:0.4pt dashed #d4d4d2;border-radius:2pt;text-align:center;color:#8a8c90;font-style:italic;font-size:8pt}
${ReportCommon.pageFooterCss(accentHex = "#ec4899", bgHex = "#f7f7f5", borderHex = "#d4d4d2")}
</style>
</head>
<body>
<div class="page-frame">
<div class="hdr">
  <div class="l">
    <div class="kicker">Academic Timeline</div>
    <h1 class="title">${data.studentName.escapeHtml()}</h1>
    <div class="subinfo"><b>${data.university.escapeHtml()}</b> · ${data.faculty.escapeHtml()} · Level ${data.level}</div>
  </div>
  <div class="r">${formatDate()}<br>GPA MANAGER</div>
</div>

<div class="strip">
  <div class="cell"><div class="k">Cumulative GPA</div><div class="v">${data.cumulativeGPA.toFixedString(2)}<span class="suf">/${data.maxGPA}</span></div></div>
  <div class="cell"><div class="k">Credits</div><div class="v">${data.totalCreditHours}<span class="suf">hrs</span></div></div>
  <div class="cell"><div class="k">Standing</div><div class="v" style="font-size:13pt;">${
        ReportCommon.resolveStanding(
            data.cumulativeGPA,
            data.maxGPA,
            data.grades
        ).symbol
    }</div></div>
  <div class="cell"><div class="k">Total Semesters</div><div class="v">${data.history.size}</div></div>
</div>

<div class="journey">
  <div class="t">The Journey at a Glance</div>
  ${
        run {
            val n = data.history.size
            if (n == 0) "<div class=\"placeholder\">No history yet</div>"
            else {
                val points = data.history.mapIndexed { i, sem ->
                    val x = if (n == 1) 200.0 else (i * 380.0 / (n - 1)) + 10.0
                    val gpa = sem.semesterGPA
                    val range = (data.maxGPA - 2.5).coerceAtLeast(0.01)
                    val y = (45 - ((gpa - 2.5).coerceAtLeast(0.0) / range) * 36).coerceIn(6.0, 45.0)
                    Triple(x, y, gpa)
                }
                val polyline =
                    points.joinToString(" ") { "${it.first.toFixedString(1)},${it.second.toFixedString(1)}" }
                val circles = points.joinToString("") { (x, y, _) ->
                    "<circle cx=\"${x.toFixedString(1)}\" cy=\"${
                        y.toFixedString(1)
                    }\" r=\"2.6\" fill=\"#ec4899\"/>"
                }
                val firstLabel = points.first().let { (x, y, gpa) ->
                    "<text x=\"${x.toFixedString(1)}\" y=\"${(y - 4).toFixedString(1)}\" font-size=\"6.5\" fill=\"#111214\" text-anchor=\"start\" font-family=\"Georgia,serif\" font-weight=\"bold\">${
                        gpa.toFixedString(2)
                    }</text>"
                }
                val lastLabel = if (n > 1) points.last().let { (x, y, gpa) ->
                    "<text x=\"${x.toFixedString(1)}\" y=\"${(y - 4).toFixedString(1)}\" font-size=\"6.5\" fill=\"#111214\" text-anchor=\"end\" font-family=\"Georgia,serif\" font-weight=\"bold\">${
                        gpa.toFixedString(2)
                    }</text>"
                } else ""
                """<svg viewBox="0 0 400 50" preserveAspectRatio="none">
            <line x1="0" y1="45" x2="400" y2="45" stroke="#d4d4d2" stroke-width="0.4"/>
            <polyline points="$polyline" fill="none" stroke="#ec4899" stroke-width="1.4" stroke-linejoin="round" stroke-linecap="round"/>
            $circles
            $firstLabel
            $lastLabel
          </svg>"""
            }
        }
    }
</div>
<div class="timeline">
  ${
        if (data.history.isNotEmpty()) {
            data.history.reversed().mapIndexed { _, sem ->
                val gradeSym =
                    ReportCommon.gradeSymbolForGpa(sem.semesterGPA, data.maxGPA, data.grades)
                """<div class="node">
            <div class="when">${sem.label}</div>
            <div class="head">
              <div class="ttl">${sem.label.escapeHtml()}</div>
              <div class="stats">
                <div class="stat"><span class="l">GPA</span><span class="v">${sem.semesterGPA.toFixedString(2)}</span></div>
                <div class="stat"><span class="l">Hrs</span><span class="v">${sem.totalCreditHours}</span></div>
                <div class="stat"><span class="l">Grade</span><span class="v">$gradeSym</span></div>
              </div>
            </div>
            ${
                    if (sem.subjects.isEmpty()) "<div class=\"placeholder\">Summary semester</div>" else renderSubjectTable(
                        sem.subjects
                    )
                }
          </div>"""
            }.joinToString("")
        } else ""
    }
</div>

</div>
${ReportCommon.pageFooterHtml(data.logoBase64Png, data.qrBase64Png)}
</body>
</html>
    """.trimIndent()

    private fun renderSubjectTable(subjects: List<com.hussienfahmy.core.domain.report.SubjectRow>): String {
        val sb = StringBuilder()
        sb.append("""<table class="subjects"><colgroup><col style="width:auto"><col style="width:7%"><col style="width:10%"><col style="width:7%"><col style="width:8%"><col style="width:8%"><col style="width:7%"><col style="width:8%"><col style="width:8%"><col style="width:12%"></colgroup><thead><tr><th>Subject</th><th>Hrs</th><th>Grade</th><th>Pts</th><th>Mid</th><th>Prac</th><th>Oral</th><th>Proj</th><th>Final</th><th>Total</th></tr></thead><tbody>""")
        subjects.forEach { row ->
            val rowClass = if (row.gradeName == null) "no-grade" else ""
            val gradeClass =
                if (row.gradeName != null) ReportCommon.gradePillClass(row.gradeName) else ""
            sb.append(
                """<tr class="$rowClass"><td class="subject-name">${row.name.escapeHtml()}</td><td>${
                    row.creditHours.toFixedString(1)
                }</td><td>${if (row.gradeName != null) """<span class="grade-pill $gradeClass">${row.gradeName.escapeHtml()}</span>""" else "—"}</td><td>${
                    if (row.gradePoints != null) row.gradePoints.toFixedString(2) else "—"
                }</td><td>${if (row.midterm != null) row.midterm.toFixedString(0) else "—"}</td><td>${
                    if (row.practical != null) row.practical.toFixedString(0) else "—"
                }</td><td>${if (row.oral != null) row.oral.toFixedString(0) else "—"}</td><td>${
                    if (row.project != null) row.project.toFixedString(0) else "—"
                }</td><td>${if (row.finalExam != null) row.finalExam.toFixedString(0) else "—"}</td><td>${
                    ReportCommon.totalMarks(
                        row
                    )?.let { "${it.first.toFixedString(0)}/${it.second.toFixedString(0)}" } ?: "—"
                }</td></tr>""")
        }
        sb.append("""</tbody></table>""")
        return sb.toString()
    }
}
