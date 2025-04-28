package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.core.model.error.InputValidationError
import com.tbacademy.core.model.error.InputValidationResult
import javax.inject.Inject

interface ValidateAddGoalTitleUseCase {
    operator fun invoke(title: String): InputValidationResult
}

class ValidateAddGoalTitleUseCaseImpl @Inject constructor() : ValidateAddGoalTitleUseCase{
    override fun invoke(title: String): InputValidationResult {
        return when{
            title.isEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)

            else -> {
                InputValidationResult.Success
            }
        }
    }

}