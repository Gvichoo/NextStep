package com.tbacademy.nextstep.presentation.screen.authentication.register.state

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFormSubmitted: Boolean = false,

    val usernameErrorMessage: Int? = null,
    val emailErrorMessage: Int? = null,
    val passwordErrorMessage: Int? = null,
    val repeatedPasswordErrorMessage: Int? = null,
) {
    val isSignUpEnabled: Boolean
        get() = emailErrorMessage == null && passwordErrorMessage == null && repeatedPasswordErrorMessage == null
}