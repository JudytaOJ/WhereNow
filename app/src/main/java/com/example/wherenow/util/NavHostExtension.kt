package com.example.wherenow.util

import android.app.Activity
import androidx.navigation.NavController

fun NavController.navigateBack() {
    if (popBackStack().not()) {
        (context as Activity?)?.finish()
    }
}