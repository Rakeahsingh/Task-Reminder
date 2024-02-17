package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen

import androidx.compose.ui.graphics.Color
import com.rkcoding.taskreminder.todo_features.domain.model.Task
import com.rkcoding.taskreminder.ui.theme.CustomGreen
import com.rkcoding.taskreminder.ui.theme.CustomRed
import com.rkcoding.taskreminder.ui.theme.Orange

data class AddTaskState(
    val currentTaskId: Int? = null,
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,
    val dueTime: Long? = null,
    val priority: Priority = Priority.LOW,
    val isTaskCompleted: Boolean = false,
    val tasks: List<Task> = emptyList()
)

enum class Priority(val title: String, val color: Color, val value: Int){

    LOW("Low", CustomGreen, 0),
    MEDIUM("Medium", Orange, 1),
    HIGH("High", CustomRed,2);

    companion object{
        fun fromInt(value: Int) = entries.firstOrNull(){ it.value == value } ?: MEDIUM
    }

}
