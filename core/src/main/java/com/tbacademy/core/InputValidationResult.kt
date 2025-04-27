package com.tbacademy.core

sealed interface InputValidationResult {
    data object Success : InputValidationResult
    data class Failure(val error: InputValidationError) : InputValidationResult
}
