package com.example.wherenow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

val SHADOW_TOOLBAR = 10.dp
const val TOOLBAR_DESCRIPTION = "Back action icon"
const val MODE = "Light or dark mode"

@Composable
fun WhereNowToolbar(
    toolbarTitle: String,
    onBackAction: () -> Unit = {},
    onChangeAppMode: () -> Unit,
    isArrowVisible: Boolean,
    isModeVisible: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .shadow(elevation = SHADOW_TOOLBAR)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isArrowVisible) {
            IconButton(
                onClick = onBackAction
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = TOOLBAR_DESCRIPTION,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        } else {
            Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space8))
        }
        Text(
            text = toolbarTitle,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1
        )
        if (isModeVisible) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.padding(end = MaterialTheme.whereNowSpacing.space4),
                onClick = onChangeAppMode
            ) {
                Icon(
                    painter = painterResource(if (isSystemInDarkTheme()) R.drawable.light_mode_icon else R.drawable.dark_mode_icon),
                    contentDescription = MODE,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowToolbarPreview() {
    WhereNowTheme {
        WhereNowToolbar(
            toolbarTitle = "Where Now",
            onBackAction = {},
            onChangeAppMode = {},
            isArrowVisible = true,
            isModeVisible = false
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowToolbarWithoutArrowPreview() {
    WhereNowTheme {
        WhereNowToolbar(
            toolbarTitle = "Where Now",
            onBackAction = {},
            onChangeAppMode = {},
            isArrowVisible = false,
            isModeVisible = true
        )
    }
}