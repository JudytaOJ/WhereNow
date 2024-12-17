package com.example.wherenow.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val value = remember { mutableStateOf(text) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value.value,
        onValueChange = { value.value = it },
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
        singleLine = true,
        readOnly = readOnly,
        isError = value.value.isEmpty(),
        trailingIcon = trailingIcon
    )
}

@PreviewLightDark
@Composable
fun WhereNowOutlinedTextFieldPreview() {
    WhereNowTheme {
        WhereNowOutlinedTextField(
            text = "Przykładowy tekst",
            placeholder = "Placeholder",
            label = "Label"
        )
    }
}