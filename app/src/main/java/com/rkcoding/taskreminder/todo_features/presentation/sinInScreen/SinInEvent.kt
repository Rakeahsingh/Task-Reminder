package com.rkcoding.taskreminder.todo_features.presentation.sinInScreen

import com.rkcoding.taskreminder.todo_features.domain.model.SinInResult

sealed class SinInEvent {

    data class SinInButtonClick(val result: SinInResult): SinInEvent()

    data object ResetState: SinInEvent()

}