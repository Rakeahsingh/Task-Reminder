package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen

import com.rkcoding.taskreminder.todo_features.domain.model.AlarmItem
import com.rkcoding.taskreminder.todo_features.domain.model.Task

sealed class TaskListEvent {

    data class DeleteTask(val task: Task): TaskListEvent()
    data class OnTaskCompleteChange(val task: Task): TaskListEvent()
    data object RestoreTask: TaskListEvent()

    data class OnSwitchValueChange(val alarmItem: AlarmItem): TaskListEvent()

}