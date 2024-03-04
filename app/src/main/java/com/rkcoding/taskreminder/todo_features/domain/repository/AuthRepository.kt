package com.rkcoding.taskreminder.todo_features.domain.repository

import com.rkcoding.taskreminder.todo_features.domain.model.UserData

interface AuthRepository {

    suspend fun loginUserWithEmailPassword(email: String, password: String): Result<UserData>
    suspend fun registerUserWithEmailPassword(name: String, email: String, password: String, profileUri: String?): Result<String>

}