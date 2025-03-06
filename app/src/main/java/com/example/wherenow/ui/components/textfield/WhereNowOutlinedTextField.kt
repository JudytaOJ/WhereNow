package com.example.wherenow.ui.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.ui.theme.WhereNowTheme

@Composable
fun WhereNowOutlinedTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String? = null,
    label: String,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        onValueChange = { onClick() },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        },
        placeholder = {
            Text(
                text = placeholder.orEmpty()
            )
        },
        readOnly = readOnly,
        isError = text.isEmpty(),
        trailingIcon = trailingIcon
    )
}

@PreviewLightDark
@Composable
fun WhereNowOutlinedTextFieldPreview() {
    WhereNowTheme {
        WhereNowOutlinedTextField(
            text = "Example text",
            placeholder = "Placeholder",
            label = "Label",
            onClick = {}
        )
    }
}