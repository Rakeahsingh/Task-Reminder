package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen

import com.rkcoding.taskreminder.todo_features.domain.model.Task

data class TaskListState(
    val tasks: List<Task> = emptyList(),
//    var switchState: Boolean = false,
    val isLoading: Boolean = false,
    val search: String = ""
)
