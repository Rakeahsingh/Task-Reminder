package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components

import android.os.Build
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    datePickerState: DatePickerState,
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {

    if (isDialogOpen){
        DatePickerDialog(
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                TextButton(onClick = { onConfirmButtonClick() }) {
                    Text(text = "Cancel")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = "Cancel")
                }
            },
            content = {
                DatePicker(
                    state = datePickerState,
                    dateValidator = { timeStamp ->
                        val selectedDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Instant
                                .ofEpochMilli(timeStamp)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        } else {
                            TODO("VERSION.SDK_INT < O")
                        }
                        val currentDate = LocalDate.now(ZoneId.systemDefault())
                        selectedDate >= currentDate
                    }
                )
            }
        )
    }

}