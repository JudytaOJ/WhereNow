package com.example.wherenow.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

val BUTTON_ELEVATION = 8.dp
val CORNER_SHAPE_50 = 50.dp
val CORNER_SHAPE_20 = 20.dp
val BUTTON_HEIGHT = 60.dp
const val BUTTON_DESCRIPTION = "Add action button"

@Composable
fun WhereNowButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContentColor = MaterialTheme.colorScheme.inverseSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceTint
    )
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(BUTTON_HEIGHT)
            .padding(MaterialTheme.whereNowSpacing.space8)
            .semantics {
                role = Role.Button
                contentDescription = BUTTON_DESCRIPTION
            },
        onClick = onClick,
        shape = RoundedCornerShape(
            topStart = CORNER_SHAPE_50,
            topEnd = CORNER_SHAPE_20,
            bottomEnd = CORNER_SHAPE_50,
            bottomStart = CORNER_SHAPE_20
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = BUTTON_ELEVATION
        ),
        colors = colors
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