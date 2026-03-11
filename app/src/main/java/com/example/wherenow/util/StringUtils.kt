package com.example.wherenow.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

class StringUtils {
    companion object {
        const val COMMA = ","
        const val SPACE = " "
        const val EMPTY = ""
        const val NEW_LINE = "\n"
    }
}

fun String.textWithFirstUppercaseChar(): String =
    lowercase().replaceFirstChar { it.uppercase() }

@Composable
fun AnnotatedString.Builder.StatisticsString(
    title: String,
    subtitle: String
) {
    withStyle(
        style = SpanStyle(
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
    ) {
        append(title)
    }
    append(StringUtils.NEW_LINE)
    withStyle(
        style = SpanStyle(
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )
    ) {
        append(subtitle)
    }
}