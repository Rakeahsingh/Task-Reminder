package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkcoding.taskreminder.core.utils.UiEvent
import com.rkcoding.taskreminder.todo_features.domain.model.Task
import com.rkcoding.taskreminder.todo_features.domain.repository.FirebaseTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val firebaseTaskRepository: FirebaseTaskRepository
): ViewModel() {


    private val _state = MutableStateFlow(TaskListState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var recentDeletedTask : Task? = null

    private var taskJob: Job? = null



    init {
        getTask()
    }


    fun onEvent(event: TaskListEvent){
        when(event){

            is TaskListEvent.DeleteTask -> deleteTask(event.task)

            TaskListEvent.RestoreTask -> {
                viewModelScope.launch {
                    firebaseTaskRepository.addTask(recentDeletedTask ?: return@launch)
                    recentDeletedTask = null
                }
            }

            is TaskListEvent.OnTaskCompleteChange -> updateTask(event.task)

            }

    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                firebaseTaskRepository.addTask(
                    task.copy(
                        isCompleted = !task.isCompleted
                    )
                )
                if (task.isCompleted){
                    _uiEvent.send(
                        UiEvent.ShowSnackBar(
                            message = "Saved in Upcoming Task Section",
                            duration = SnackbarDuration.Short
                        )
                    )
                } else{
                    _uiEvent.send(
                        UiEvent.ShowSnackBar(
                            message = "Saved in Completed Task Section",
                            duration = SnackbarDuration.Short
                        )
                    )
                }
            }catch (e: Exception){
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "Task couldn't update",
                        duration = SnackbarDuration.Short
                    )
                )
            }
        }
    }


    private fun deleteTask(task: Task){
        viewModelScope.launch {
            try {
                firebaseTaskRepository.deleteTask(task.taskId)
                recentDeletedTask = task
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "Task deleted Successfully",
                        duration = SnackbarDuration.Long
                    )
                )
            }catch (e: Exception){
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "Task can't be deleted",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }


    private fun getTask(){
        viewModelScope.launch {
            firebaseTaskRepository.getTask()
        }
    }

}