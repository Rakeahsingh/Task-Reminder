package com.rkcoding.taskreminder.todo_features.presentation.sinInScreen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkcoding.taskreminder.core.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SinInViewModel @Inject constructor(): ViewModel(){

    private val _state = MutableStateFlow(SinInState())
    val state = _state.asStateFlow()

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    fun onEvent(event: SinInEvent){
        when(event){
            is SinInEvent.SinInButtonClick -> {
                viewModelScope.launch {
                    try {
                        _state.update {
                            it.copy(
                                isSinInSuccess = event.result.data != null,
                                sinInError = event.result.errorMessage
                            )
                        }
                        _snackBarEvent.send(
                            SnackBarEvent.ShowSnackBar(
                                message = "SinIn Successfully",
                                duration = SnackbarDuration.Short
                            )
                        )
                    }catch (e: Exception){
                        _snackBarEvent.send(
                            SnackBarEvent.ShowSnackBar(
                                message = "SinIn Successfully",
                                duration = SnackbarDuration.Long
                            )
                        )
                    }
                }

            }

            SinInEvent.ResetState -> {
                _state.update {
                    SinInState()
                }
            }
        }
    }

}