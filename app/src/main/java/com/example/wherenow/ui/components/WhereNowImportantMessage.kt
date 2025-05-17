package com.example.wherenow.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

val TONAL_ELEVATION = 4.dp
val SHADOW_ELEVATION = 6.dp
val ICON_SIZE = 24.dp

@Composable
fun WhereNowImportantMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.secondary,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = TONAL_ELEVATION,
        shadowElevation = SHADOW_ELEVATION,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.whereNowSpacing.space16,
                vertical = MaterialTheme.whereNowSpacing.space8
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space16)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(ICON_SIZE)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.whereNowSpacing.space8))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.primaryContainer,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowImportantMessagePreview() {
    WhereNowTheme {
        WhereNowImportantMessage(
            message = "Remember that you can only add files in .pdf format"
        )
    }
}