package com.hussienfahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.presentation.components.ExpandableTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowRowDivider
import com.hussienfahmy.core_ui.theme.MeadowTheme

@Composable
fun AcademicStatusSection(
    level: Int,
    semester: UserData.AcademicInfo.Semester,
    onLevelChange: (String) -> Unit,
    onSemesterChange: (UserData.AcademicInfo.Semester) -> Unit,
    modifier: Modifier = Modifier
) {
    MeadowCard(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            CapsLabel(text = stringResource(Res.string.onboarding_academic_status_title))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.onboarding_academic_status_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MeadowTheme.colors.inkFaint,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        ExpandableTextField(
            title = stringResource(Res.string.level),
            value = level.toString(),
            onNewValueSubmitted = onLevelChange,
            keyboardType = KeyboardType.Number,
        )
        MeadowRowDivider()
        SemesterSelection(
            semester = semester,
            onSemesterClick = onSemesterChange,
        )
    }
}
