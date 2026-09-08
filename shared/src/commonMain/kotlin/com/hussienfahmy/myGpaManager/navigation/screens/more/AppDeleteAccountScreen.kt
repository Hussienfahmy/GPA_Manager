package com.hussienfahmy.myGpaManager.navigation.screens.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.LocalScaffoldContentPadding
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowConfirmationSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppDeleteAccountScreen(
    snackBarHostState: SnackbarHostState,
    viewModel: DeleteAccountViewModel = koinViewModel(),
) {
    UiEventHandler(uiEvent = viewModel.uiEvent, snackBarHostState = snackBarHostState)

    val isAnonymous by viewModel.isAnonymous.collectAsStateWithLifecycle(null)

    DeleteAccountScreenContent(
        modifier = Modifier.fillMaxSize(),
        isAnonymous = isAnonymous == true,
        isDeleting = viewModel.isDeleting,
        onDeleteConfirmed = viewModel::deleteAccount,
    )
}

@Composable
fun DeleteAccountScreenContent(
    isAnonymous: Boolean,
    isDeleting: Boolean,
    onDeleteConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val scaffoldPadding = LocalScaffoldContentPadding.current

    val confirmToken = stringResource(Res.string.delete_account_confirm_token)
    var typed by rememberSaveable { mutableStateOf("") }
    var showSheet by remember { mutableStateOf(false) }
    val tokenMatches = typed.trim().equals(confirmToken, ignoreCase = true)

    Box(modifier = modifier) {
        MeadowAccentProvider(colors.more) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .padding(bottom = scaffoldPadding.calculateBottomPadding()),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(colors.dangerContainer),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteForever,
                            contentDescription = null,
                            tint = colors.onDangerContainer,
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    Text(
                        text = stringResource(Res.string.delete_account_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.ink,
                    )
                }

                Text(
                    text = stringResource(
                        if (isAnonymous) Res.string.delete_account_warning_local
                        else Res.string.delete_account_warning_synced
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.chipText,
                )

                MeadowCard {
                    DeletionBullet(stringResource(Res.string.delete_account_bullet_profile))
                    Spacer(Modifier.height(8.dp))
                    DeletionBullet(stringResource(Res.string.delete_account_bullet_grades))
                    Spacer(Modifier.height(8.dp))
                    DeletionBullet(stringResource(Res.string.delete_account_bullet_history))
                    Spacer(Modifier.height(8.dp))
                    DeletionBullet(stringResource(Res.string.delete_account_bullet_settings))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(colors.dangerContainer)
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WarningAmber,
                        contentDescription = null,
                        tint = colors.onDangerContainer,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = stringResource(Res.string.delete_account_irreversible),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onDangerContainer,
                    )
                }

                Spacer(Modifier.height(2.dp))

                Text(
                    text = stringResource(Res.string.delete_account_type_to_confirm, confirmToken),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.ink,
                )

                MeadowTextField(
                    value = typed,
                    onValueChange = { typed = it },
                    label = confirmToken,
                    enabled = !isDeleting,
                    outlined = true,
                )

                PillButton(
                    text = stringResource(Res.string.delete_account_cta),
                    onClick = { showSheet = true },
                    style = PillButtonStyle.Danger,
                    enabled = tokenMatches && !isDeleting,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        if (isDeleting) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .background(colors.paper.copy(alpha = 0.92f)),
                verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(color = colors.more.deep)
                Text(
                    text = stringResource(Res.string.delete_account_in_progress),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.chipText,
                )
            }
        }
    }

    if (showSheet) {
        MeadowConfirmationSheet(
            title = stringResource(Res.string.delete_account_sheet_title),
            body = stringResource(Res.string.delete_account_sheet_body),
            confirmText = stringResource(Res.string.delete_account_sheet_confirm),
            onConfirm = onDeleteConfirmed,
            onDismiss = { showSheet = false },
        )
    }
}

@Composable
private fun DeletionBullet(text: String) {
    Text(
        text = "•  $text",
        style = MaterialTheme.typography.bodyMedium,
        color = MeadowTheme.colors.chipText,
    )
}

@Composable
private fun DeleteAccountShowcase(isAnonymous: Boolean) {
    Column(modifier = Modifier.fillMaxWidth().background(MeadowTheme.colors.paper)) {
        DeleteAccountScreenContent(
            isAnonymous = isAnonymous,
            isDeleting = false,
            onDeleteConfirmed = {},
        )
    }
}

@Preview(name = "DeleteAccount · light")
@Composable
private fun DeleteAccountLightPreview() {
    MeadowTheme(darkTheme = false) { DeleteAccountShowcase(isAnonymous = false) }
}

@Preview(name = "DeleteAccount · dark")
@Composable
private fun DeleteAccountDarkPreview() {
    MeadowTheme(darkTheme = true) { DeleteAccountShowcase(isAnonymous = false) }
}

@Preview(name = "DeleteAccount · guest · AR", locale = "ar")
@Composable
private fun DeleteAccountArPreview() {
    MeadowTheme(darkTheme = false) { DeleteAccountShowcase(isAnonymous = true) }
}
