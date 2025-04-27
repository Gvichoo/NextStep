package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.core.InputValidationError
import com.tbacademy.core.InputValidationResult
import javax.inject.Inject

interface ValidateMilestoneUseCase {
    operator fun invoke(milestone : String) : InputValidationResult
}

class ValidateMilestoneUseCaseImpl @Inject constructor() : ValidateMilestoneUseCase{
    override fun invoke(milestone: String): InputValidationResult {
        return when{
            milestone.isEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)

            else -> {
                InputValidationResult.Success
            }
        }
    }
}