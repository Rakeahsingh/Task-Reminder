package com.rkcoding.taskreminder.todo_features.presentation.todoTaskAddScreen

import androidx.lifecycle.ViewModel
import com.rkcoding.taskreminder.core.utils.SnackBarEvent
import com.rkcoding.taskreminder.todo_features.domain.repository.FirebaseTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: FirebaseTaskRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddTaskState())
    val state = _state.asStateFlow()

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    fun onEvent(event: AddTaskEvent){
        when(event){
            AddTaskEvent.DeleteTask -> {

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

            }
        }
    }

}