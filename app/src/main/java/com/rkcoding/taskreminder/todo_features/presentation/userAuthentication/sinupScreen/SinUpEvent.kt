package com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinupScreen

sealed class SinUpEvent {

    data class OnNameValueChange(val name: String): SinUpEvent()
    data class OnEmailValueChange(val email: String): SinUpEvent()
    data class OnPasswordValueChange(val password: String): SinUpEvent()
    data class OnConfirmPasswordValueChange(val confirmPassword: String): SinUpEvent()
    data object SinUpButtonClick: SinUpEvent()


}