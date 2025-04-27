package com.tbacademy.core

sealed interface InputValidationError {
    // General
    data object Empty : InputValidationError
    data object Invalid : InputValidationError

    // Email
    data object InvalidEmailFormat : InputValidationError

    // Username
    data object UsernameShort : InputValidationError
    data object UsernameLong : InputValidationError
    data object InvalidUsernameFormat : InputValidationError

    // Password
    data object PasswordShort : InputValidationError
    data object PasswordWeak : InputValidationError
    data object PasswordsDoNotMatch : InputValidationError

}
