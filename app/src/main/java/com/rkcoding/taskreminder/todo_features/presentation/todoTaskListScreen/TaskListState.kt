package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen

import com.rkcoding.taskreminder.todo_features.domain.model.Task

data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val switchState: Boolean = false
)
