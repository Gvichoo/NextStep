package com.tbacademy.core.model.error

sealed interface InputValidationResult {
    data object Success : InputValidationResult
    data class Failure(val error: InputValidationError) : InputValidationResult
}
