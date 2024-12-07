package com.example.wherenow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

@Composable
fun WhereNowTextField(
    value: String,
    label: String,
    valueTextStyle: TextStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary)
) {
    var text by rememberSaveable { mutableStateOf(value) }

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.Start
    ) {
        BasicTextField(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.whereNowSpacing.space8),
            value = label,
            onValueChange = {},
            singleLine = true,
            readOnly = true,
            textStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space2))
        BasicTextField(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.whereNowSpacing.space8),
            value = text,
            onValueChange = { newText -> text = newText },
            singleLine = true,
            readOnly = true,
            textStyle = valueTextStyle
            )
    }
}

@PreviewLightDark
@Composable
fun WhereNowTextFieldPreview() {
    WhereNowTheme {
        WhereNowTextField(
            value = "Warszawa",
            label = "Miasto"
        )
    }
}