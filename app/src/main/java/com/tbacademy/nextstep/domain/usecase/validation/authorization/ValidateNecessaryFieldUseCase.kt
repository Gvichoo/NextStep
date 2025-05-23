package com.tbacademy.nextstep.domain.usecase.validation.authorization

import com.tbacademy.core.model.error.InputValidationError
import com.tbacademy.core.model.error.InputValidationResult
import javax.inject.Inject

interface ValidateNecessaryFieldUseCase {
    operator fun invoke(input: String): InputValidationResult
}

class ValidateNecessaryFieldUseCaseImpl @Inject constructor() : ValidateNecessaryFieldUseCase {
    override fun invoke(input: String): InputValidationResult {
        return if (input.isEmpty()) {
            InputValidationResult.Failure(error = InputValidationError.Empty)
        } else {
            InputValidationResult.Success
        }
    }
}