package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rkcoding.taskreminder.core.utils.toDateFormat
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.PriorityBottomSheet
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.TaskDatePicker
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.TaskTimePicker
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.TaskTopBar
import com.rkcoding.taskreminder.ui.theme.CustomBlue
import java.time.Instant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController,
    viewModel: AddTaskViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val priority = listOf(
        "Low", "Medium", "High"
    )

//    var title by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
    var isTitleError by rememberSaveable { mutableStateOf<String?>(null) }
    var isDescriptionError by rememberSaveable { mutableStateOf<String?>(null) }

    // title error text
    isTitleError = when{
        state.title.isBlank() -> "Please Enter Task Title"
        state.title.length < 4 -> "Task Title is too Short"
        state.title.length > 20 -> "Task Title is too Long"
        else -> null
    }

    // description error text
    isDescriptionError = when{
        state.description.isBlank() -> "Please Enter Task Description"
        state.description.length < 8 -> "Task Description is too Short"
        state.description.length > 100 -> "Task Description is too Long"
        else -> null
    }

    // bottomSheet remember
    var showBottomSheet by remember { mutableStateOf(false) }
    // bottomSheet state
    val sheetState = rememberModalBottomSheetState()

    // show task time dialog remember
    var timePickerDialog by remember { mutableStateOf(false) }

    // initial state hour and minute
    val selectedHour by remember { mutableIntStateOf(0) }
    val selectedMinute by remember { mutableIntStateOf(0) }

    // task Timer State
    val timePickerState = rememberTimePickerState(
        initialHour = selectedHour,
        initialMinute = selectedMinute
    )

    // show date picker dialog remember
    var datePickerDialog by remember { mutableStateOf(false) }

    // Task Date picker state
    val datePickerState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        rememberDatePickerState(
            initialSelectedDateMillis = Instant.now().toEpochMilli()
        )
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    // show BottomSheet
    PriorityBottomSheet(
        state = sheetState,
        isSheetShow = showBottomSheet,
        priority = priority,
        onItemClick = {
            showBottomSheet = false
        },
        onDismissRequest = {
            showBottomSheet = false
        }
    )

    // show Date picker Dialog
    TaskDatePicker(
        datePickerState = datePickerState,
        isDialogOpen = datePickerDialog,
        onDismissRequest = { datePickerDialog = false },
        onConfirmButtonClick = {
            viewModel.onEvent(AddTaskEvent.OnDueDateChange(date = datePickerState.selectedDateMillis))
            datePickerDialog = false
        }
    )

    // show task time picker Dialog
    TaskTimePicker(
        timePickerState = timePickerState,
        isDialogOpen = timePickerDialog,
        title = "Select Time!",
        onDismissRequest = { timePickerDialog = false },
        onConfirmButtonClick = {
            viewModel.onEvent(AddTaskEvent.OnDueTimeChange(millis = selectedHour.toLong()))
            timePickerDialog = false
        }
    )

    Scaffold(
        topBar = {
            TaskTopBar(
                isTaskExits = true,
                isComplete = true,
                checkBoxBorderColor = Color.Red,
                onBackIconClick = { navController.popBackStack() },
                onCheckBoxClick = {
                    viewModel.onEvent(AddTaskEvent.IsTaskCompleted)
                },
                onDeleteIconClick = {
                    viewModel.onEvent(AddTaskEvent.DeleteTask)
                }
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
                value = state.title,
                onValueChange = {
                    viewModel.onEvent(AddTaskEvent.OnTitleChange(it))
                },
                label = { Text(text = "Title") },
                singleLine = true,
                isError = isTitleError != null && state.title.isNotBlank(),
                supportingText = { Text(text = isTitleError.orEmpty()) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = {
                    viewModel.onEvent(AddTaskEvent.OnDescriptionChange(it))
                },
                label = { Text(text = "Description") },
                singleLine = true,
                isError = isDescriptionError != null && state.description.isNotBlank(),
                supportingText = { Text(text = isDescriptionError.orEmpty()) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Due Date")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = state.dueDate.toDateFormat(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic
                )

                IconButton(
                    onClick = { datePickerDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date picker"
                    )
                }
            }

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "Due Time")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$selectedHour:$selectedMinute",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic
                )
                IconButton(
                    onClick = { timePickerDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Date picker"
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Medium",
                    style = MaterialTheme.typography.bodyLarge
                )

                IconButton(onClick = { showBottomSheet = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "DropDown Icon"
                    )
                }
            }

            Button(
                onClick = { viewModel.onEvent(AddTaskEvent.SaveTask) },
                enabled = isTitleError == null && isDescriptionError == null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Save Task",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }

}