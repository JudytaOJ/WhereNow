package com.example.wherenow.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.ui.theme.WhereNowTheme

val BUTTON_ELEVATION = 8.dp
val CORNER_SHAPE_50 = 50.dp
val CORNER_SHAPE_20 = 20.dp
const val BUTTON_DESCRIPTION = "Add action button"

@Composable
fun WhereNowButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(8.dp)
            .semantics { contentDescription = BUTTON_DESCRIPTION },
        onClick = onClick,
        shape = RoundedCornerShape(
            topStart = CORNER_SHAPE_50,
            topEnd = CORNER_SHAPE_20,
            bottomEnd = CORNER_SHAPE_50,
            bottomStart = CORNER_SHAPE_20
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = BUTTON_ELEVATION
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