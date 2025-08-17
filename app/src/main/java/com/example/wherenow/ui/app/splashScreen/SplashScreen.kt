package com.example.wherenow.ui.app.splashScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.navigation.navigateToTripList
import com.example.wherenow.ui.theme.Size
import com.example.wherenow.ui.theme.WhereNowTheme
import kotlinx.coroutines.delay

const val ANIMATION_LENGTH = 5000L

@Composable
internal fun SplashScreen(
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        delay(ANIMATION_LENGTH)
        navController.navigateToTripList()
    }

    val splashAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.splash_screen))
    val splashAnimationProgress by animateLottieCompositionAsState(
        composition = splashAnimation,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = splashAnimation,
            alignment = Alignment.Center,
            progress = splashAnimationProgress,
            modifier = Modifier.size(Size().size300)
        )
    }
}

@PreviewLightDark
@Composable
private fun SplashScreenPreview() {
    WhereNowTheme {
        val navController = rememberNavController()
        SplashScreen(navController = navController)
    }
}