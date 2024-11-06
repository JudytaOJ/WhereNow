package com.example.wherenow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.navigation.WhereNowNavHost
import com.example.wherenow.ui.theme.WhereNowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhereNowTheme {
                val navController = rememberNavController()
                WhereNowNavHost(navController = navController)
            }
        }
    }
}