package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rkcoding.taskreminder.core.utils.toDateFormat
import com.rkcoding.taskreminder.todo_features.domain.model.Task
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.Priority
import com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen.components.TaskCheckBox


@Composable
fun TaskCardItem(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskCardClick: (Task) -> Unit,
//    onDeleteTaskClick: (Task) -> Unit,
    onCheckBoxClick: (Task) -> Unit,
    switchState: Boolean,
    onSwitchValueChange: () -> Unit
) {


    ElevatedCard(
        modifier = modifier
            .clickable { onTaskCardClick(task) }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TaskCheckBox(
                isComplete = task.isCompleted,
                borderColor = Priority.fromInt(value = task.priority).color,
                onCheckBoxClick = { onCheckBoxClick(task) }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted){
                        TextDecoration.LineThrough
                    } else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(4.dp))


                Text(
                    text = task.dueDate.toDateFormat(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = switchState,
                onCheckedChange = { onSwitchValueChange() }
            )

//            IconButton(
//                onClick = { onDeleteTaskClick(task) }
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = "delete task"
//                )
//            }

        }

    }

}