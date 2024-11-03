package com.example.wherenow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme

val BUTTON_SHADOW = 10.dp

@Composable
fun WhereNowMainButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    textButton: String,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = BUTTON_SHADOW),
        onClick = onClick,
        colors = ButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.scrim,
            disabledContainerColor = MaterialTheme.colorScheme.scrim
        ),
        enabled = enabled,
        border = BorderStroke(
            color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer,
            width = 1.dp
        )
    ) {
        Text(
            text = textButton,
            style = MaterialTheme.typography.titleMedium,
            color = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@PreviewLightDark
@Composable
private fun WhereNowMainButtonEnabledPreview() {
    WhereNowTheme {
        WhereNowMainButton(
            onClick = {},
            textButton = stringResource(R.string.button_text)
        )
    }
}

@PreviewLightDark
@Composable
private fun WhereNowMainButtonDisabledPreview() {
    WhereNowTheme {
        WhereNowMainButton(
            onClick = {},
            textButton = stringResource(R.string.button_text),
            enabled = false
        )
    }
}