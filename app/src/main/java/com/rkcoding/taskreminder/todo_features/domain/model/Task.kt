package com.rkcoding.taskreminder.todo_features.domain.model



data class Task(
    val taskId: Int,
    val title: String,
    val description: String,
    val dueDate: Long,
    val dueTime: String,
    val priority: Int,
    val isCompleted: Boolean
)
