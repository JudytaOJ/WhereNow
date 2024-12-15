package com.example.wherenow.ui.app.error

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.ui.components.WhereNowButton
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

@Composable
fun ErrorScreen(
    onClick: () -> Unit
) {
    BackHandler(true) { /*do nothing*/ }

    val errorAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.error)
    )

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        content = { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
                        .padding(top = MaterialTheme.whereNowSpacing.space40)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        composition = errorAnimation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        iterations = LottieConstants.IterateForever,
                    )
                    Text(
                        modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space16),
                        text = stringResource(R.string.error_description),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.weight(1f))
                    WhereNowButton(
                        buttonText = stringResource(R.string.close),
                        onClick = onClick,
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                            disabledContentColor = MaterialTheme.colorScheme.inverseSurface,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceTint
                        )
                    )
                }
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun ErrorPreview() {
    WhereNowTheme {
        ErrorScreen(
            onClick = {}
        )
    }
}