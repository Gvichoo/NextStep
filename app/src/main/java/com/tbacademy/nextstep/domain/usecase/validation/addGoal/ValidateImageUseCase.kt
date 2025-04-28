package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.core.model.error.InputValidationError
import com.tbacademy.core.model.error.InputValidationResult
import javax.inject.Inject

interface ValidateImageUseCase {
    operator fun invoke(imageUri: String?): InputValidationResult
}

class ValidateImageUseCaseImpl @Inject constructor() : ValidateImageUseCase {
    override fun invoke(imageUri: String?): InputValidationResult {
        return when {
            imageUri.isNullOrEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            else -> InputValidationResult.Success
        }
    }
}