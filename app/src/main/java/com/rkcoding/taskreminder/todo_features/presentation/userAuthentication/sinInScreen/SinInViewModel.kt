package com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinInScreen

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkcoding.taskreminder.core.utils.UiEvent
import com.rkcoding.taskreminder.todo_features.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SinInViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel(){

    private val _state = MutableStateFlow(SinInState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SinInEvent){
        when(event){
            is SinInEvent.GoogleSinInButtonClick -> {
                viewModelScope.launch {
                    try {
                        _state.update {
                            it.copy(
                                isSinInSuccess = event.result.data != null,
                                sinInError = event.result.errorMessage
                            )
                        }
                        _uiEvent.send(
                            UiEvent.NavigateTo
                        )
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
                                message = "SinIn Successfully",
                                duration = SnackbarDuration.Short
                            )
                        )
                    } catch (e: Exception) {
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
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

            is SinInEvent.OnEmailValueChange -> {
                _state.update {
                    it.copy(
                        userEmail = event.email
                    )
                }
            }

            is SinInEvent.OnPasswordValueChange -> {
                _state.update {
                    it.copy(
                        userPassword = event.password
                    )
                }
            }

            SinInEvent.SinInButtonClick -> sinInWithEmail()

        }
    }

    private fun sinInWithEmail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                repository.loginUserWithEmailPassword(
                    email = _state.value.userEmail,
                    password = _state.value.userPassword
                )
                    .onSuccess {
                        _state.update { it.copy(isLoading = false) }

                        _uiEvent.send(
                            UiEvent.NavigateTo
                        )

                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
                                message = "SinUp Successfully...",
                                duration = SnackbarDuration.Short
                            )
                        )
                    }
                    .onFailure { exception ->
                        Log.e("YourTag", "SinIn Failed with error", exception)
                        _state.update { it.copy(isLoading = false) }
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
                                message = "SinUp Failed with error...",
                                duration = SnackbarDuration.Short
                            )
                        )
                    }
            }catch (e: Exception){
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "SinIn Failed...",
                        duration = SnackbarDuration.Short
                    )
                )
            }

        }
    }


}