package com.tbacademy.nextstep.presentation.screen.authentication.login.state

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val resetPasswordEmail : String = ""
)