package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen

import com.rkcoding.taskreminder.todo_features.domain.model.Task

sealed class TaskListEvent {

    data class DeleteTask(val task: Task): TaskListEvent()
    data class OnTaskCompleteChange(val task: Task): TaskListEvent()
    data object RestoreTask: TaskListEvent()

    data class OnSwitchValueChange(val task: Task): TaskListEvent()

    data class OnSearchValueChange(val text: String): TaskListEvent()

    data object OnSearchIconClick: TaskListEvent()

}