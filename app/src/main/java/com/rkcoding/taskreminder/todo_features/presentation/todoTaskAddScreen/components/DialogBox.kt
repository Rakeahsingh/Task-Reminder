package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DialogBox(
    text: String,
    bodyText: String,
    iconImage: ImageVector,
    isDialogShow: Boolean,
    onConfirmButtonClick: () -> Unit,
    onDismissRequest: () -> Unit
) {

    if (isDialogShow){
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                TextButton(onClick = { onConfirmButtonClick() }) {
                    Text(text = "Delete")
                }
            },
            title = {
                Text(text = text)
            },
            text = {
                Text(text = bodyText)
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = "Cancel")
                }
            },
            icon = {
                Icon(
                    imageVector = iconImage,
                    contentDescription = "dialog icon"
                )
            }
        )
    }

}