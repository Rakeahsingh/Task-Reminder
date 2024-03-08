package com.rkcoding.taskreminder.todo_features.presentation.todoTaskListScreen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkcoding.taskreminder.core.utils.UiEvent
import com.rkcoding.taskreminder.todo_features.domain.model.AlarmItem
import com.rkcoding.taskreminder.todo_features.domain.model.Task
import com.rkcoding.taskreminder.todo_features.domain.repository.AlarmScheduler
import com.rkcoding.taskreminder.todo_features.domain.repository.FirebaseTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: FirebaseTaskRepository,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {


    private val _state = MutableStateFlow(TaskListState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var recentDeletedTask : Task? = null

    private var searchJob: Job? = null


    init {
        viewModelScope.launch {
            repository.realTimeTaskData()
//            repository.getTask()
            getTask()
        }
    }


    fun onEvent(event: TaskListEvent){
        when(event){

            is TaskListEvent.DeleteTask -> deleteTask(event.task)

            TaskListEvent.RestoreTask -> {
                viewModelScope.launch {
                    repository.addTask(recentDeletedTask ?: return@launch)
                    recentDeletedTask = null
                }
            }

            is TaskListEvent.OnTaskCompleteChange -> updateTask(event.task)

            is TaskListEvent.OnSwitchValueChange -> {
                switchChange(event.task)
            }

            TaskListEvent.OnSearchIconClick -> {
                onSearch()
            }
            is TaskListEvent.OnSearchValueChange -> {
                _state.update {
                    it.copy(
                        search = event.text
                    )
                }
            }
        }
    }

    private fun onSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
        }
    }

    private fun switchChange(task: Task) {
        viewModelScope.launch {
            try {
                val alarmDeferred = async {
                    repository.addTask(
                        task.copy(isScheduled = !task.isScheduled)
                    )
                }
                if (!task.isScheduled){
                    alarmScheduler.schedule(
                        AlarmItem(
                            alarmTime = getLocalDateTime(task.dueDate, task.dueTime),
                            message = task.description
                        )
                    )
                    _uiEvent.send(
                        UiEvent.ShowSnackBar(
                            message = "Alarm is Scheduled",
                            duration = SnackbarDuration.Short
                        )
                    )
                }

                alarmDeferred.await()
                if (task.isScheduled){
                    alarmScheduler.cancel(
                        AlarmItem(
                            alarmTime = getLocalDateTime(task.dueDate, task.dueTime),
                            message = task.description
                        )
                    )
                    _uiEvent.send(
                        UiEvent.ShowSnackBar(
                            message = "Alarm is Cancel",
                            duration = SnackbarDuration.Short
                        )
                    )
                }


            }catch (e: Exception){
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "Alarm can't be Schedule",
                        duration = SnackbarDuration.Short
                    )
                )
            }

        }
    }


    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.addTask(
                    task.copy(
                        isCompleted = !task.isCompleted
                    )
                )
                if (task.isCompleted){
                    _uiEvent.send(
                        UiEvent.ShowSnackBar(
                            message = "Saved in Incomplete Task Section",
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
                repository.deleteTask(task.taskId)
                recentDeletedTask = task
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



    private suspend fun getTask(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
             repository.tasks.collectLatest { task ->
                _state.update {
                   it.copy(
                       tasks = task,
                       isLoading = false
                   )
                }
            }
        }
    }

    private fun getLocalDateTime(dueDate: Long, dueTime: String): LocalDateTime {
        val dateFormatter = DateTimeFormatter.ofPattern("HH:mm")

//        val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        // Convert dueDate to LocalDateTime
        val dueDateTime = LocalDateTime.ofEpochSecond(dueDate / 1000, 0, ZoneOffset.UTC)

        // Parse dueTime and combine with dueDate
        val localTime = LocalTime.parse(dueTime, dateFormatter)
        return LocalDateTime.of(dueDateTime.toLocalDate(), localTime)
    }

}