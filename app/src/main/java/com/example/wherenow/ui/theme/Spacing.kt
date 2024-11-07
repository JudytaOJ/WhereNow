package com.example.wherenow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class WhereNowSpacing(
    val space2: Dp = 2.dp,
    val space4: Dp = 4.dp,
    val space8: Dp = 8.dp,
    val space16: Dp = 16.dp,
    val space24: Dp = 24.dp,
    val space32: Dp = 32.dp
)

internal val LocalSpacing = compositionLocalOf { WhereNowSpacing() }

val MaterialTheme.whereNowSpacing: WhereNowSpacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current