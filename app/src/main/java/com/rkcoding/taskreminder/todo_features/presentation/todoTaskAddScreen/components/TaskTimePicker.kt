package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTimePicker(
    timePickerState: TimePickerState,
    isDialogOpen: Boolean,
    title: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {


    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmButtonClick()
                    }
                ) {
                    Text(text = "OK")
                }
            },
            title = {
                Text(text = title)
            },
            text = {
                   // time picker
                   TimePicker(
                       state = timePickerState
                   )
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissRequest() }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }

}