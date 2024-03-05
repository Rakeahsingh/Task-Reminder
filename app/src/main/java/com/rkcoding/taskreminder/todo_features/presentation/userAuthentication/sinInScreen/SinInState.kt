package com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinInScreen

data class SinInState(
    val isSinInSuccess: Boolean = false,
    val sinInError: String? = null,
    val userEmail: String = "",
    val userPassword: String = "",
    val isLoading: Boolean = false
)
