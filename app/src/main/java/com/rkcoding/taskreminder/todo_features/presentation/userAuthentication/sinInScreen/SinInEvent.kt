package com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinInScreen

import com.rkcoding.taskreminder.todo_features.domain.model.SinInResult

sealed class SinInEvent {

    data class GoogleSinInButtonClick(val result: SinInResult): SinInEvent()
    data class OnEmailValueChange(val email: String): SinInEvent()
    data class OnPasswordValueChange(val password: String): SinInEvent()
    data object SinInButtonClick: SinInEvent()

    data object ResetState: SinInEvent()

}