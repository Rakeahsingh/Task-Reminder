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
import androidx.compose.material.icons.filled.Alarm
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rkcoding.taskreminder.core.navigation.Screen
import com.rkcoding.taskreminder.core.utils.SnackBarEvent
import com.rkcoding.taskreminder.core.utils.toDateFormat
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.DeleteDialog
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.PriorityButton
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.TaskDatePicker
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.TaskTimePicker
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.TaskTopBar
import com.rkcoding.taskreminder.ui.theme.CustomBlue
import com.rkcoding.taskreminder.ui.theme.DarkBlue
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController,
    viewModel: AddTaskViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()


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

    //show delete Dialog remember
    var deleteDialog by remember { mutableStateOf(false) }

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

    // show Delete Dialog
    DeleteDialog(
        text = "Delete Task",
        bodyText = "Are you sure you want to Delete this Task",
        isDialogShow = deleteDialog,
        onConfirmButtonClick = {
            deleteDialog = false
            viewModel.onEvent(AddTaskEvent.DeleteTask)
        },
        onDismissRequest = { deleteDialog = false }
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
            viewModel.onEvent(AddTaskEvent.OnDueTimeChange(
                millis = "${timePickerState.hour}:${timePickerState.minute}"
            ))
            timePickerDialog = false
        }
    )

    // show SnackBar Event
    LaunchedEffect(key1 = true){
        viewModel.snackBarEvent.collectLatest { event ->
            when(event){
                SnackBarEvent.Navigate -> {
                    navController.navigate(Screen.TaskListScreen.route)
                }
                SnackBarEvent.NavigateUp -> Unit
                is SnackBarEvent.ShowSnackBar -> {
                    SnackBarEvent.ShowSnackBar(
                        message = event.message,
                        duration = event.duration
                    )
                }
            }
        }
    }



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
                    deleteDialog = true
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

            Text(
                text = "Due Date:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

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
                        contentDescription = "Date picker",
                        tint = DarkBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Due Time:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.dueTime ?: "select reminder Time" ,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic
                )
                IconButton(
                    onClick = { timePickerDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = "Date picker",
                        tint = DarkBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Priority:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Priority.entries.forEach { priority ->
                    PriorityButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        label = priority.title,
                        backgroundColor = priority.color,
                        borderColor = if (priority == state.priority) Color.White else Color.Transparent,
                        labelColor = if (priority == state.priority) Color.White else Color.White.copy(alpha = 0.7f),
                        onItemClick = {
                            viewModel.onEvent(AddTaskEvent.OnPriorityChange(priority))
                        }
                    )
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = {
                    viewModel.onEvent(AddTaskEvent.SaveTask)
                },
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