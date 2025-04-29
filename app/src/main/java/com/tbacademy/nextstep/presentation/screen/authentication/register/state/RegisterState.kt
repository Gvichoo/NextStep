package com.tbacademy.nextstep.presentation.screen.authentication.register.state

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val formBeenSubmitted: Boolean = false,

    val usernameErrorMessage: Int? = null,
    val emailErrorMessage: Int? = null,
    val passwordErrorMessage: Int? = null,
    val repeatedPasswordErrorMessage: Int? = null,
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
) {
    val isSignUpEnabled: Boolean
        get() = emailErrorMessage == null && passwordErrorMessage == null && repeatedPasswordErrorMessage == null
}