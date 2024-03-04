package com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinupScreen

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
class SinUpViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(SinUpState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SinUpEvent){
        when(event){

            is SinUpEvent.OnConfirmPasswordValueChange -> {
                _state.update {
                    it.copy(
                        userConfirmPassword = event.confirmPassword
                    )
                }
            }

            is SinUpEvent.OnEmailValueChange -> {
                _state.update {
                    it.copy(
                        userEmail = event.email
                    )
                }
            }

            is SinUpEvent.OnNameValueChange -> {
                _state.update {
                    it.copy(
                        userName = event.name
                    )
                }
            }

            is SinUpEvent.OnPasswordValueChange -> {
                _state.update {
                    it.copy(
                        userPassword = event.password
                    )
                }
            }

            SinUpEvent.SinUpButtonClick -> {
                if (_state.value.userName.isNotBlank()){
                    sinUp()
                }
            }

        }
    }

    private fun sinUp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                if (_state.value.userPassword != _state.value.userConfirmPassword){
                    return@launch
                }else{
                    repository.registerUserWithEmailPassword(
                        name = _state.value.userName,
                        email = _state.value.userEmail,
                        password = _state.value.userPassword,
                        profileUri = _state.value.userProfileImage
                    )
                        .onSuccess {
                            _state.update { it.copy(isLoading = false) }
                            _uiEvent.send(
                                UiEvent.ShowSnackBar(
                                    message = "SinUp Successfully",
                                    duration = SnackbarDuration.Short
                                )
                            )
                            _uiEvent.send(
                                UiEvent.NavigateTo
                            )
                        }
                        .onFailure {
                            _state.update { it.copy(isLoading = false) }
                            _uiEvent.send(
                                UiEvent.ShowSnackBar(
                                    message = "SinUp Failed",
                                    duration = SnackbarDuration.Short
                                )
                            )
                        }
                }
            }catch (e: Exception){
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        message = "SinUp Failed",
                        duration = SnackbarDuration.Short
                    )
                )
            }
        }
    }

}