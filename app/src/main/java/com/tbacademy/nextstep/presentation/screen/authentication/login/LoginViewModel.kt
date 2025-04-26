package com.tbacademy.nextstep.presentation.screen.authentication.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.login.LoginUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateEmailUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateNecessaryFieldUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.getErrorMessageResId
import com.tbacademy.nextstep.presentation.screen.authentication.login.effect.LoginEffect
import com.tbacademy.nextstep.presentation.screen.authentication.login.event.LoginEvent
import com.tbacademy.nextstep.presentation.screen.authentication.login.state.LoginState
import com.tbacademy.nextstep.presentation.screen.authentication.login.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCaseImpl,
    private val validateNecessaryFieldUseCase: ValidateNecessaryFieldUseCase,

    ) : BaseViewModel<LoginState, LoginEvent, LoginEffect, LoginUiState>(
    initialState = LoginState(),
    initialUiState = LoginUiState()
) {

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> onEmailChanged(email = event.email)
            is LoginEvent.PasswordChanged -> onPasswordChanged(password = event.password)
            is LoginEvent.RememberMeChanged -> onRememberMeChanged(rememberMe = event.rememberMe)
            is LoginEvent.Submit -> submitLogInForm()
            is LoginEvent.RegisterButtonClicked -> viewModelScope.launch {
                emitEffect(LoginEffect.NavToRegisterFragment)
            }
        }
    }

    // On Email Update
    private fun onEmailChanged(email: String) {
        updateUiState { this.copy(email = email) }

        val emailValidationResult = validateInputOnChange { validateEmailUseCase(email = email) }
        val emailErrorMessage: Int? = emailValidationResult?.getErrorMessageResId()

        updateState { this.copy(emailErrorMessage = emailErrorMessage) }
    }

    // On Password Update
    private fun onPasswordChanged(password: String) {
        updateUiState { this.copy(password = password) }

        val passwordValidationResult =
            validateInputOnChange { validateNecessaryFieldUseCase(input = password) }
        val passwordErrorMessage: Int? = passwordValidationResult?.getErrorMessageResId()

        updateState { this.copy(passwordErrorMessage = passwordErrorMessage) }
    }

    // On Remember Me Update
    private fun onRememberMeChanged(rememberMe: Boolean) {
        updateUiState { this.copy(rememberMe = rememberMe) }
    }

    // On Submit
    private fun submitLogInForm() {

        val formIsValid = validateForm(
            email = uiState.value.email,
            password = uiState.value.password
        )

        if (formIsValid) {
            loginUser(
                email = uiState.value.email,
                password = uiState.value.password,
                rememberMe = uiState.value.rememberMe
            )
        } else {
            updateState { this.copy(formBeenSubmitted = true) }
        }

    }

    // Form Validation
    private fun validateForm(
        email: String,
        password: String
    ): Boolean {

        // Validate Inputs
        val emailValidationError = validateEmailUseCase(email = email).getErrorMessageResId()
        val passwordValidationError =
            validateNecessaryFieldUseCase(input = password).getErrorMessageResId()

        // Update states of errors
        updateState {
            copy(
                emailErrorMessage = emailValidationError,
                passwordErrorMessage = passwordValidationError
            )
        }

        // Check if form is valid
        val errors: List<Int?> = listOf(
            emailValidationError,
            passwordValidationError
        )

        return errors.all { it == null }
    }

    private fun loginUser(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            loginUseCase(email = email, password = password, rememberMe = rememberMe)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            Log.d("LOG_IN_ERROR", "${result.error}")
                            emitEffect(LoginEffect.ShowError(result.error.toMessageRes()))
                        }

                        is Resource.Loading -> {
                            updateState { copy(isLoading = result.loading) }
                        }

                        is Resource.Success -> {
                            updateState { copy(isSuccess = true) }
                            emitEffect(LoginEffect.NavToMainFragment)
                        }
                    }
                }
        }
    }

    // Helpers
    private fun validateInputOnChange(validator: () -> InputValidationResult): InputValidationResult? {
        return if (state.value.formBeenSubmitted)
            validator()
        else null
    }
}