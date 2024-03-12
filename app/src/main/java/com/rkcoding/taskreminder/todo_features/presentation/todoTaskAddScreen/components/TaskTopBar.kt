package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTopBar(
//    isTaskExits: Boolean,
//    isComplete: Boolean,
//    checkBoxBorderColor: Color,
    onBackIconClick: () -> Unit,
//    onCheckBoxClick: () -> Unit,
//    onDeleteIconClick: () -> Unit
) {

    TopAppBar(
        navigationIcon = {
             IconButton(
                 onClick = onBackIconClick
             ) {
                 Icon(
                     imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                     contentDescription = "back navigate"
                 )
             }
        },
        title = {
            Text(
                text = "Task Reminder",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
//        actions = {
////            if (isTaskExits){
//                TaskCheckBox(
//                    isComplete = isComplete,
//                    borderColor = checkBoxBorderColor,
//                    onCheckBoxClick = onCheckBoxClick
//                )
//
//                IconButton(
//                    onClick = onDeleteIconClick
//                ) {
//                   Icon(
//                       imageVector = Icons.Default.Delete,
//                       contentDescription = "Delete"
//                   )
//                }
////            }
//        }
    )


}