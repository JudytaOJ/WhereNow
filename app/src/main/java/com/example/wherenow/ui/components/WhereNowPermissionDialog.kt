package com.example.wherenow.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme

@Composable
fun WhereNowPermissionDialog(
    confirmButton: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.permission_dialog_title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.inverseSurface
            )
        },
        text = {
            Text(
                text = stringResource(R.string.permission_dialog_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.inverseSurface
            )
        },
        confirmButton = {
            TextButton(
                onClick = { confirmButton() }
            ) {
                Text(
                    text = stringResource(R.string.permission_dialog_settings_button),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.trip_details_calendar_cancel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun WhereNowPermissionDialogPreview() {
    WhereNowTheme {
        WhereNowPermissionDialog(
            onDismiss = {},
            confirmButton = {}
        )
    }
}