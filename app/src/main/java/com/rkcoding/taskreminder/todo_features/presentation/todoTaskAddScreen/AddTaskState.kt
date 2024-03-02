package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen

import androidx.compose.ui.graphics.Color
import com.rkcoding.taskreminder.ui.theme.CustomGreen
import com.rkcoding.taskreminder.ui.theme.Orange
import java.util.Date

data class AddTaskState(
    val currentTaskId: String = Date().time.toString(),
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,
    val dueTime: String? = null,
    val priority: Priority = Priority.LOW,
    val isTaskCompleted: Boolean = false,
    val isScheduled : Boolean = false,
)

enum class Priority(val title: String, val color: Color, val value: Int){

    LOW("Low", CustomGreen, 0),
    MEDIUM("Medium", Orange, 1),
    HIGH("High", Color.Red,2);

    companion object{
        fun fromInt(value: Int) = entries.firstOrNull{ it.value == value } ?: MEDIUM
    }

}
