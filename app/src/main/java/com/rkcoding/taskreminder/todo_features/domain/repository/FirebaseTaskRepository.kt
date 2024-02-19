package com.rkcoding.taskreminder.todo_features.domain.repository

import com.rkcoding.taskreminder.todo_features.domain.model.Task
import kotlinx.coroutines.flow.StateFlow

interface FirebaseTaskRepository {

    val tasks : StateFlow<List<Task>>

    suspend fun getTask(): List<Task>
    suspend fun addTask(task: Task)
    suspend fun deleteTask(taskId: String)
    suspend fun getTaskById(id: Int): Task?

    suspend fun realTimeTaskData()

    fun stopTaskRealTimeUpdate()

}