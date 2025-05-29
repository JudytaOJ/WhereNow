package com.example.wherenow.ui.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.StringUtils

@Composable
fun WhereNowEditableTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String = StringUtils.EMPTY,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    maxLines: Int = Int.MAX_VALUE,
    onChangeValue: (String) -> Unit,
    contentDescriptionAccessibility: String = StringUtils.EMPTY
) {
    var titleTextField by remember { mutableStateOf(text) }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.whereNowSpacing.space8)
            .semantics { contentDescription = contentDescriptionAccessibility },
        value = titleTextField,
        onValueChange = {
            onChangeValue(it)
            titleTextField = it
        },
        textStyle = textStyle,
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle.copy(color = MaterialTheme.colorScheme.outline)
            )
        },
        colors = customTextFieldColors(),
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun customTextFieldColors(
    isSearchBar: Boolean = false
): TextFieldColors =
    TextFieldColors(
        focusedTextColor = if (!isSearchBar) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimaryContainer,
        focusedContainerColor = Color.Transparent,
        unfocusedTextColor = if (!isSearchBar) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimaryContainer,
        disabledTextColor = Color.Transparent,
        errorTextColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        cursorColor = if (!isSearchBar) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimaryContainer,
        errorCursorColor = Color.Transparent,
        textSelectionColors = TextSelectionColors(
            handleColor = Color.Transparent,
            backgroundColor = Color.Transparent
        ),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedLeadingIconColor = Color.Transparent,
        unfocusedLeadingIconColor = Color.Transparent,
        disabledLeadingIconColor = Color.Transparent,
        errorLeadingIconColor = Color.Transparent,
        focusedTrailingIconColor = Color.Transparent,
        unfocusedTrailingIconColor = Color.Transparent,
        disabledTrailingIconColor = Color.Transparent,
        errorTrailingIconColor = Color.Transparent,
        focusedLabelColor = Color.Transparent,
        unfocusedLabelColor = Color.Transparent,
        disabledLabelColor = Color.Transparent,
        errorLabelColor = Color.Transparent,
        focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
        disabledPlaceholderColor = Color.Transparent,
        errorPlaceholderColor = Color.Transparent,
        focusedSupportingTextColor = Color.Transparent,
        unfocusedSupportingTextColor = Color.Transparent,
        disabledSupportingTextColor = Color.Transparent,
        errorSupportingTextColor = Color.Transparent,
        focusedPrefixColor = Color.Transparent,
        unfocusedPrefixColor = Color.Transparent,
        disabledPrefixColor = Color.Transparent,
        errorPrefixColor = Color.Transparent,
        focusedSuffixColor = Color.Transparent,
        unfocusedSuffixColor = Color.Transparent,
        disabledSuffixColor = Color.Transparent,
        errorSuffixColor = Color.Transparent
    )

@PreviewLightDark
@Composable
private fun WhereNowEditableTextFieldPreview() {
    WhereNowTheme {
        Surface {
            WhereNowEditableTextField(
                text = "First title for preview",
                onChangeValue = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun WhereNowEditableTextFieldLongTitlePreview() {
    WhereNowTheme {
        Surface {
            WhereNowEditableTextField(
                text = LoremIpsum(30).values.joinToString(),
                onChangeValue = {}
            )
        }
    }
}