package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.TaskTopBar

@Composable
fun AddTaskScreen(
    navController: NavController
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isTitleError by rememberSaveable { mutableStateOf<String?>(null) }
    var isDescriptionError by rememberSaveable { mutableStateOf<String?>(null) }

    isTitleError = when{
        title.isBlank() -> "Please Enter Task Title"
        title.length < 4 -> "Task Title is too Short"
        title.length > 20 -> "Task Title is too Long"
        else -> null
    }

    isDescriptionError = when{
        description.isBlank() -> "Please Enter Task Description"
        description.length < 8 -> "Task Description is too Short"
        description.length > 100 -> "Task Description is too Long"
        else -> null
    }

    Scaffold(
        topBar = {
            TaskTopBar(
                isTaskExits = true,
                isComplete = true,
                checkBoxBorderColor = Color.Red,
                onBackIconClick = { navController.popBackStack() },
                onCheckBoxClick = { /*TODO*/ },
                onDeleteIconClick = {  }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(10.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Title") },
                singleLine = true,
//                isError = isTitleError != null, title.isNotBlank(),
//                supportingText = {Text(text = isTitleError.orEmpty())}
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Description") },
                singleLine = true
            )

        }
    }

}