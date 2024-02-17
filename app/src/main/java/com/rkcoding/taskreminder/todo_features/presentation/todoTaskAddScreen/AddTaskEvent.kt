package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen

sealed class AddTaskEvent {

    data class OnTitleChange(val title: String): AddTaskEvent()
    data class OnDescriptionChange(val description: String): AddTaskEvent()
    data class OnDueDateChange(val date: Long?): AddTaskEvent()
    data class OnDueTimeChange(val millis: Long?): AddTaskEvent()
    data class OnPriorityChange(val priority: Priority): AddTaskEvent()
    data object IsTaskCompleted: AddTaskEvent()
    data object SaveTask: AddTaskEvent()
    data object DeleteTask: AddTaskEvent()

}