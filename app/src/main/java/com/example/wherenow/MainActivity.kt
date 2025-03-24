package com.example.wherenow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.ui.theme.WhereNowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WhereNowTheme {
                NavHost(
                    onCloseApp = { finish() }
                )
            }
        }
    }
}