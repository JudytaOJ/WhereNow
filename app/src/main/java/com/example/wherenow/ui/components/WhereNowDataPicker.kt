package com.example.wherenow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.convertLongToTime
import com.example.wherenow.util.testutil.TestTag.DATA_PICKER_TAG

@Composable
fun WhereNowDataPicker(
    modifier: Modifier = Modifier,
    date: Long,
    onUpdateDate: (Long?) -> Unit
) {
    var showModal by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val (first, second) = FocusRequester.createRefs()

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
            .focusOrder(first) {
                this.down = second
                focusManager.clearFocus()
            }
            .semantics(mergeDescendants = true) {
                disabled()
            }
            .testTag(DATA_PICKER_TAG),
        value = convertLongToTime(date),
        onValueChange = {},
        label = {
            Text(
                text = stringResource(R.string.trip_details_date),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.trip_details_format_date)
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.semantics { role = Role.Button },
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select date",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
            errorContainerColor = Color.Transparent
        )
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { onUpdateDate(it) },
            onDismiss = { showModal = false },
            date = date
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    date: Long
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.trip_details_calendar_select),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.trip_details_calendar_cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@PreviewLightDark
@Composable
private fun DatePickerDockedPreview() {
    WhereNowTheme {
        WhereNowDataPicker(
            date = 23062025,
            onUpdateDate = {}
        )
    }
}
