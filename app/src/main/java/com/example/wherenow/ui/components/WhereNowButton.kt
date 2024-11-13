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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.ui.theme.WhereNowTheme

val BUTTON_ELEVATION = 10.dp
val BUTTON_BORDER = 1.dp

@Composable
fun WhereNowButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = BUTTON_ELEVATION,
                shape = MaterialTheme.shapes.small,
            ),
        onClick = onClick,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
            width = BUTTON_BORDER,
            color = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowButtonPreview() {
    WhereNowTheme {
        WhereNowButton(
            onClick = {},
            buttonText = "Dalej"
        )
    }
}