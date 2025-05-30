package com.tbacademy.nextstep.presentation.screen.authentication.login.state

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val formBeenSubmitted: Boolean = false,
    val errorMessage: String? = null,

    val emailErrorMessage: Int? = null,
    val passwordErrorMessage: Int? = null,

    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false
) {
    val isLogInEnabled: Boolean
        get() =  emailErrorMessage == null && passwordErrorMessage == null
}