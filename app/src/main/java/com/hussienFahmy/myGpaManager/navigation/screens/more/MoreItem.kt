package com.hussienfahmy.myGpaManager.navigation.screens.more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hussienfahmy.core_ui.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreItem(
    onClick: () -> Unit,
    title: String,
    summary: String,
) {
    val spacing = LocalSpacing.current

    Card(
        shape = RoundedCornerShape(spacing.small),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(spacing.small)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(spacing.small))

            Text(
                text = summary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}