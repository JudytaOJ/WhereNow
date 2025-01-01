package com.example.wherenow.ui.app.error

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.wherenow.navigation.AppDestination
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.navigation.Screen
import com.example.wherenow.util.testutil.TestTag.LOTTIE_ANIMATION
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ErrorScreenTest {

    private lateinit var navController: TestNavHostController

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun beforeEach() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavHost(
                navController = navController,
                onCloseApp = {}
            )
            navController.navigate(AppDestination.ErrorScreen.route)
        }
    }

    @Test
    fun check_lottie_animation_and_description_is_visible() {
        composeTestRule.onNodeWithTag(LOTTIE_ANIMATION).assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Oops, something went wrong…").assertIsDisplayed()
    }

    @Test
    fun check_button_is_visible_and_navigation_to_list_trip_screen() {
        composeTestRule.onNodeWithTag("buttonTag").assertIsDisplayed().assertTextEquals("Close").performClick()
        Assert.assertEquals(Screen.LIST_TRIP.name, navController.currentBackStackEntry?.destination?.route)
    }
}