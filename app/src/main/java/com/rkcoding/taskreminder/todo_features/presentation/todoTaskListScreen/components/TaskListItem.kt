package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rkcoding.taskreminder.core.navigation.Screen
import com.rkcoding.taskreminder.todo_features.domain.model.Task
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen.TaskListEvent
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen.TaskListViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListItem(
    snackBarState: SnackbarHostState,
    task: Task,
    viewModel: TaskListViewModel = hiltViewModel(),
    navController: NavController,
) {

    val scope = rememberCoroutineScope()

    // swipe to dismiss
    val dismissState = rememberDismissState()

    // check if user Swipe
    if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {

        viewModel.onEvent(TaskListEvent.DeleteTask(task))


        scope.launch {
            val result = snackBarState.showSnackbar(
                message = "Task Deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.onEvent(TaskListEvent.RestoreTask)
            }
        }
    }

    // swipe to delete functionality
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            // background color
            val backgroundColor by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.DismissedToStart -> Color.Red.copy(alpha = 0.8f)
                    else -> MaterialTheme.colorScheme.background
                },
                label = "background color animation"
            )
            // icon size
            val iconScale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.DismissedToStart) 1.3f else 0.5f,
                label = "icon animation"
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor)
                    .padding(end = 16.dp), // inner padding
                contentAlignment = Alignment.CenterEnd // place the icon at the end (left)
            ) {
                Icon(
                    modifier = Modifier.scale(iconScale),
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        dismissContent = {
            TaskCardItem(
                modifier = Modifier.padding(vertical = 8.dp),
                task = task,
                onTaskCardClick = {
                    navController.navigate(
                        route = Screen.AddTaskScreen.route + "?taskId=${task.taskId}"
                    )
                },
                onCheckBoxClick = {
                    viewModel.onEvent(TaskListEvent.OnTaskCompleteChange(task))
                },
                switchState = task.isScheduled,
                onSwitchValueChange = {
                    viewModel.onEvent(TaskListEvent.OnSwitchValueChange(task))
                }
            )
        }
    )

}