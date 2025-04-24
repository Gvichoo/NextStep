package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import javax.inject.Inject

interface ImageValidator {
    operator fun invoke(imageUri: String?): InputValidationResult
}

class ImageValidatorImpl @Inject constructor() : ImageValidator {
    override fun invoke(imageUri: String?): InputValidationResult {
        return when {
            imageUri.isNullOrEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            else -> InputValidationResult.Success
        }
    }
}