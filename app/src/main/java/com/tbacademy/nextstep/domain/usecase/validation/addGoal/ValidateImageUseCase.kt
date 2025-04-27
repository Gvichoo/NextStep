package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
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