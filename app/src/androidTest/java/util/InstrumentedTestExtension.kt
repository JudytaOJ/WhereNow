package util

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry

fun ComposeTestRule.assertTextIsDisplayed(@androidx.annotation.StringRes stringResId: Int) {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val text = context.getString(stringResId)
    onNodeWithText(text).assertIsDisplayed()
}

fun ComposeTestRule.assertTextIsNotDisplayed(@androidx.annotation.StringRes stringResId: Int) {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val text = context.getString(stringResId)
    onNodeWithText(text).assertIsNotDisplayed()
}

fun ComposeTestRule.waitFewSeconds(durationMillis: Long = 3000L) {
    mainClock.advanceTimeBy(durationMillis)
    waitForIdle()
}
