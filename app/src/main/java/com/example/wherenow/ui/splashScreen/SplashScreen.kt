package com.example.wherenow.ui.splashScreen

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.navigation.Screen
import com.example.wherenow.ui.theme.WhereNowTheme
import kotlinx.coroutines.delay

val SIZE_ANIMATION = 400.dp
const val ANIMATION_LENGTH = 5000L

@Composable
fun SplashScreen(
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        delay(ANIMATION_LENGTH)
        navController.navigate(Screen.HOME.name)
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
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = splashAnimation,
            alignment = Alignment.Center,
            progress = splashAnimationProgress,
            modifier = Modifier.size(SIZE_ANIMATION)
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