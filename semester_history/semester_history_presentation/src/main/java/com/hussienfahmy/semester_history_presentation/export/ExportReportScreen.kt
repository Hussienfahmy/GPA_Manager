package com.hussienfahmy.semester_history_presentation.export

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.report.ReportTemplate

@Composable
fun ExportReportSheetContent(
    state: ExportReportState,
    onEvent: (ExportReportEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Text(
                stringResource(R.string.export_select_template),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                stringResource(R.string.export_archived_only_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            TemplateGrid(
                selectedTemplate = state.selectedTemplate,
                onSelectTemplate = { onEvent(ExportReportEvent.SelectTemplate(it)) },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Surface(
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { onEvent(ExportReportEvent.Export) },
                enabled = !state.isExporting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                if (state.isExporting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(stringResource(R.string.export_pdf))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TemplateGrid(
    selectedTemplate: ReportTemplate,
    onSelectTemplate: (ReportTemplate) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 2,
    ) {
        ReportTemplate.entries.forEach { template ->
            TemplateCard(
                template = template,
                isSelected = template == selectedTemplate,
                onClick = { onSelectTemplate(template) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun TemplateCard(
    template: ReportTemplate,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant

    val shape = RoundedCornerShape(12.dp)
    val bg = Color(template.backgroundColorHex)
    val accent = Color(template.accentColorHex)
    val textOnBg = if (template.isDark) Color.White else Color(0xFF14181F)

    Column(
        modifier = modifier
            .clip(shape)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = shape,
            )
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(bg),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(accent),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 14.dp, end = 8.dp),
            ) {
                Text(
                    stringResource(template.displayNameRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = textOnBg,
                )
                Text(
                    stringResource(template.taglineRes),
                    style = MaterialTheme.typography.labelSmall,
                    color = textOnBg.copy(alpha = 0.7f),
                )
            }
        }
        Text(
            stringResource(template.descriptionRes),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
            minLines = 3,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(12.dp),
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun ExportReportSheetContentPreview() {
    MaterialTheme {
        ExportReportSheetContent(
            state = ExportReportState(
                selectedTemplate = ReportTemplate.LEDGER,
                isExporting = false
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, heightDp = 800, name = "Exporting")
@Composable
private fun ExportReportSheetContentExportingPreview() {
    MaterialTheme {
        ExportReportSheetContent(
            state = ExportReportState(selectedTemplate = ReportTemplate.MODERN, isExporting = true),
            onEvent = {},
        )
    }
}
