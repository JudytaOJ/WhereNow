package com.example.wherenow.util

class StringUtils {
    companion object {
        const val COMMA = ","
        const val SPACE = " "
        const val EMPTY = ""
    }
}

fun String.textWithFirstUppercaseChar(): String =
    lowercase().replaceFirstChar { it.uppercase() }