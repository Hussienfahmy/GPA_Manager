package com.hussienfahmy.semester_history_presentation.export

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.domain.report.ReportTemplate
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.theme.MeadowTheme

@Composable
fun ExportReportSheetContent(
    state: ExportReportState,
    onEvent: (ExportReportEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.export_select_template),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.export_archived_only_note),
            style = MaterialTheme.typography.bodySmall,
            color = colors.inkFaint,
        )

        Spacer(modifier = Modifier.height(14.dp))

        TemplateGrid(
            selectedTemplate = state.selectedTemplate,
            onSelectTemplate = { onEvent(ExportReportEvent.SelectTemplate(it)) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isExporting) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.height(28.dp),
                    strokeWidth = 3.dp,
                    color = MeadowTheme.accent.accent,
                )
            }
        } else {
            PillButton(
                text = stringResource(Res.string.export_pdf),
                onClick = { onEvent(ExportReportEvent.Export) },
                style = PillButtonStyle.Primary,
                modifier = Modifier.fillMaxWidth(),
            )
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
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
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val shape = RoundedCornerShape(16.dp)
    val bg = Color(template.backgroundColorHex)
    val stripe = Color(template.accentColorHex)
    val textOnBg = if (template.isDark) Color.White else Color(0xFF14181F)

    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.card)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accent.accent else colors.divider,
                shape = shape,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
    ) {
        // Colored preview swatch — the template's own palette
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
                    .background(stripe),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 14.dp, end = 8.dp),
            ) {
                Text(
                    text = stringResource(template.displayNameRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = textOnBg,
                )
                Text(
                    text = stringResource(template.taglineRes),
                    style = MaterialTheme.typography.labelSmall,
                    color = textOnBg.copy(alpha = 0.7f),
                )
            }
        }
        Text(
            text = stringResource(template.descriptionRes),
            style = MaterialTheme.typography.bodySmall,
            color = colors.inkMuted,
            modifier = Modifier.padding(12.dp),
        )
    }
}

@Preview(showBackground = true, heightDp = 700)
@Composable
private fun ExportReportSheetContentPreview() {
    MeadowTheme(darkTheme = false) {
        Column(modifier = Modifier.background(MeadowTheme.colors.card).padding(20.dp)) {
            ExportReportSheetContent(
                state = ExportReportState(
                    selectedTemplate = ReportTemplate.LEDGER,
                    isExporting = false
                ),
                onEvent = {},
            )
        }
    }
}
