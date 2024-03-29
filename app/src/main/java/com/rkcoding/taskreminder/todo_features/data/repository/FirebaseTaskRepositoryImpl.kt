package com.rkcoding.taskreminder.todo_features.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.rkcoding.taskreminder.todo_features.domain.model.Task
import com.rkcoding.taskreminder.todo_features.domain.repository.FirebaseTaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class FirebaseTaskRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
): FirebaseTaskRepository {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    override val tasks: StateFlow<List<Task>>
        get() = _tasks.asStateFlow()

    private val tasksListenerRegistration: ListenerRegistration? = null

    override suspend fun getTask(): List<Task> {
        val userId = firebaseAuth.currentUser?.uid ?: return emptyList()

        return fireStore.collection("users")
            .document(userId)
            .collection("tasks")
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                Task(
                    taskId = document.getString("taskId") ?: return@mapNotNull null,
                    title = document.getString("title") ?: "",
                    description = document.getString("description") ?: "",
                    dueDate = document.getLong("dueDate") ?: 0L,
                    dueTime = document.getString("dueTime") ?: "",
                    priority = document.getLong("priority")?.toInt() ?: 1,
                    isCompleted = document.getBoolean("isCompleted") ?: false,
                    isScheduled = document.getBoolean("scheduled") ?: false
                )
            }.reversed()
    }

    override suspend fun addTask(task: Task) {
        if (task.taskId.isNotEmpty()){
            Log.d("TAG", "TaskId: ${task.taskId}")
            val userId = firebaseAuth.currentUser?.uid ?: return
            fireStore.collection("users")
                .document(userId)
                .collection("tasks")
                .document(task.taskId)
                .set(task, SetOptions.merge())
                .await()
        }else{
            Log.d("TAG", "Task id is null: ${task.taskId}")
        }
        
    }

    override suspend fun deleteTask(taskId: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val query = fireStore.collection("users")
            .document(userId)
            .collection("tasks")
            .whereEqualTo("taskId", taskId)

        query.get().addOnSuccessListener {
            for (document in it){
                document.reference.delete()
            }
        }.await()
    }

    override suspend fun getTaskById(id: String): Task? {
        val userId = firebaseAuth.currentUser?.uid ?: return null

        try {
            val querySnapshot = fireStore.collection("users")
                .document(userId)
                .collection("tasks")
                .whereEqualTo("taskId", id)
                .get()
                .await()

            val documents = querySnapshot.documents

            if (documents.isNotEmpty()){
                val document = documents[0]
                val taskId = document.getString("taskId") ?: return null
                val title = document.getString("title") ?: ""
                val description = document.getString("description") ?: ""
                val dueDate = document.getLong("dueDate") ?: 0L
                val dueTime = document.getString("dueTime") ?: ""
                val priority = document.getLong("priority")?.toInt() ?: 1
                val isCompleted = document.getBoolean("completed") ?: false
                val isScheduled = document.getBoolean("scheduled") ?: false

                return Task(taskId, title, description, dueDate, dueTime, priority, isCompleted, isScheduled)

            }else{
                return null
            }

        }catch (e: Exception){
            e.printStackTrace()
            e.message
            return null
        }
    }

    override suspend fun realTimeTaskData() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        // Ensure previous listener is removed before setting up a new one
        stopTaskRealTimeUpdate()

        val query = fireStore.collection("users")
            .document(userId)
            .collection("tasks")

        query.addSnapshotListener{querySnapshot, firebaseFireStoreException ->
            if (firebaseFireStoreException != null){
                return@addSnapshotListener
            }
            val tasks = mutableListOf<Task>()
            querySnapshot?.documents?.forEach { document ->
                Log.d("document", "Document data: ${document.data}")
                val taskId = document.getString("taskId") ?: return@forEach
                val title = document.getString("title") ?: ""
                val description = document.getString("description") ?: ""
                val dueDate = document.getLong("dueDate") ?: 0L
                val dueTime = document.getString("dueTime") ?: ""
                val priority = document.getLong("priority")?.toInt() ?: 1
                val isCompleted = document.getBoolean("completed") ?: false
                val isScheduled = document.getBoolean("scheduled") ?: false

                val task = Task(taskId, title, description, dueDate, dueTime, priority, isCompleted, isScheduled)
                Log.d("TAG", "realTimeTaskData: ${task.taskId}")
                tasks.add(task)
            }
            _tasks.value = tasks
        }

    }

    override fun stopTaskRealTimeUpdate() {
        tasksListenerRegistration?.remove()
    }
}