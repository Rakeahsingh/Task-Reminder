package com.rkcoding.taskreminder.todo_features.domain.model

data class SinInResult(
    val data: UserData?,
    val errorMessage: String?
)


data class UserData(
    val userId: String,
    val userName: String?,
    val userEmail: String?,
    val userPassword: String?,
    val profileImageUrl: String?
)
