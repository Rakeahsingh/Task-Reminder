package com.rkcoding.taskreminder.core.utils

import androidx.compose.material3.SnackbarDuration

sealed class UiEvent {

    data class ShowSnackBar(
        val message: String,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ): UiEvent()

    data object NavigateUp: UiEvent()

    data object NavigateTo: UiEvent()

}