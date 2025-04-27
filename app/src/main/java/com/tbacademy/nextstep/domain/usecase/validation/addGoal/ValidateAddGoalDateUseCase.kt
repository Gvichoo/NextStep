package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.core.InputValidationError
import com.tbacademy.core.InputValidationResult
import java.util.Date
import javax.inject.Inject

interface ValidateAddGoalDateUseCase {
    operator fun invoke(date: Date?): InputValidationResult
}

class ValidateAddGoalDateUseCaseImpl @Inject constructor() : ValidateAddGoalDateUseCase {
    override fun invoke(date: Date?): InputValidationResult {
        return when {
            date == null -> InputValidationResult.Failure(error = InputValidationError.Empty)

            else -> InputValidationResult.Success
        }
    }
}