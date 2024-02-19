package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkcoding.taskreminder.core.navigation.Screen
import com.rkcoding.taskreminder.core.utils.UiEvent
import com.rkcoding.taskreminder.todo_features.domain.model.Task
import com.rkcoding.taskreminder.todo_features.domain.repository.FirebaseTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: FirebaseTaskRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddTaskState())
    val state = _state.asStateFlow()

    private val _snackBarEvent = Channel<UiEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    fun onEvent(event: AddTaskEvent){
        when(event){

            AddTaskEvent.DeleteTask -> {
                deleteTask()
            }

            AddTaskEvent.IsTaskCompleted -> {
                _state.update {
                    it.copy(
                        isTaskCompleted = !_state.value.isTaskCompleted
                    )
                }
            }

            is AddTaskEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }

            is AddTaskEvent.OnDueDateChange -> {
                _state.update {
                    it.copy(
                        dueDate = event.date
                    )
                }
            }

            is AddTaskEvent.OnDueTimeChange -> {
                _state.update {
                    it.copy(
                        dueTime = event.millis
                    )
                }
            }

            is AddTaskEvent.OnPriorityChange -> {
                _state.update {
                    it.copy(
                        priority = event.priority
                    )
                }
            }

            is AddTaskEvent.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            AddTaskEvent.SaveTask -> {
                saveTask()
            }
        }
    }

    private fun saveTask(){
        viewModelScope.launch {
            try {
                repository.addTask(
                    Task(
                        taskId = _state.value.currentTaskId ?: 0,
                        title = _state.value.title,
                        description = _state.value.description,
                        dueDate = _state.value.dueDate ?: 0L,
                        dueTime = _state.value.dueTime ?: "",
                        priority = _state.value.priority.value,
                        isCompleted = _state.value.isTaskCompleted
                    )
                )
                _snackBarEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "Task saved Successfully",
                        duration = SnackbarDuration.Short
                    )
                )
                _snackBarEvent.send(
                    UiEvent.NavigateTo
                )
            }catch (e: Exception){
                _snackBarEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "Task Can't be save",
                        duration = SnackbarDuration.Long
                    )
                )
            }

        }
    }


    private fun deleteTask(){
        viewModelScope.launch {
            try {
                repository.deleteTask(
                    taskId = _state.value.currentTaskId ?: return@launch
                )
                _snackBarEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "Task Deleted Successfully",
                        duration = SnackbarDuration.Short
                    )
                )
            }catch (e: Exception){
                _snackBarEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "Task Can't be Deleted",
                        duration = SnackbarDuration.Long
                    )
                )
            }

        }
    }

}