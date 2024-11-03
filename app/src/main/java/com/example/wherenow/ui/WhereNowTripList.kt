package com.example.wherenow.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wherenow.R
import com.example.wherenow.navigation.Screen
import com.example.wherenow.ui.components.WhereNowDetailsFlightTile
import com.example.wherenow.ui.components.WhereNowDetailsTile
import com.example.wherenow.ui.components.WhereNowMainButton
import com.example.wherenow.ui.components.WhereNowModalBottomSheet
import com.example.wherenow.ui.components.WhereNowToolbar
import java.time.LocalDate

//Temporary solution
@Composable
fun WhereNowTripList(
    navController: NavController
) {
    BackHandler {
        navController.navigate(Screen.HOME) {
            popUpTo(Screen.HOME) {
                inclusive = true
            }
        }
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = "Where now",
                onBackAction = {},
                onChangeAppMode = {}
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Spacer(modifier = Modifier.padding(24.dp))
            WhereNowDetailsTile(
                city = "Lizbona",
                country = "Portugalia",
                date = "20 listopad 2024",
                timeTravel = LocalDate.now().plusDays(4),
                onClick = {}
            )
            WhereNowDetailsFlightTile(
                cardDescription = "Szczegóły lotu",
                image = painterResource(R.drawable.flight_icon),
                onClick = {}
            )
            WhereNowModalBottomSheet()
            WhereNowMainButton(
                onClick = {},
                textButton = "Dodaj"
            )
        }
    }
}