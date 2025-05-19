package com.example.wherenow.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

@Composable
fun WhereNowFloatingActionButton(
    image: ImageVector = Icons.Default.Add,
    onClick: () -> Unit,
    contentDescriptionForAccessibility: String = stringResource(R.string.accessibility_add_trip)
) {
    FloatingActionButton(
        modifier = Modifier
            .padding(MaterialTheme.whereNowSpacing.space8)
            .semantics { role = Role.Button },
        containerColor = MaterialTheme.colorScheme.tertiary,
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = image,
            contentDescription = contentDescriptionForAccessibility,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowFloatingActionButtonPreview() {
    WhereNowTheme {
        WhereNowFloatingActionButton(
            onClick = {}
        )
    }
}