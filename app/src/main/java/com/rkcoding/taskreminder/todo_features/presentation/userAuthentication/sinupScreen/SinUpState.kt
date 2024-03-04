package com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinupScreen

data class SinUpState(
    val userName: String = "",
    val userEmail: String = "",
    val userPassword: String = "",
    val userConfirmPassword: String = "",
    val userProfileImage: String? = null,
    val isLoading: Boolean = false
)
