package util

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry

fun ComposeTestRule.assertTextIsDisplayed(@StringRes stringResId: Int) {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val text = context.getString(stringResId)
    onNodeWithText(text).assertIsDisplayed()
}

fun ComposeTestRule.assertTextIsNotDisplayed(@StringRes stringResId: Int) {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val text = context.getString(stringResId)
    onNodeWithText(text).assertIsNotDisplayed()
}

fun ComposeTestRule.waitFewSeconds(durationMillis: Long = 3000L) {
    mainClock.advanceTimeBy(durationMillis)
    waitForIdle()
}

fun ComposeTestRule.scrollToNodeWithTag(tag: String) {
    onNodeWithTag(tag)
        .performScrollTo()
}

fun ComposeTestRule.scrollToNodeWithText(text: String) {
    onNodeWithText(text)
        .performScrollTo()
}