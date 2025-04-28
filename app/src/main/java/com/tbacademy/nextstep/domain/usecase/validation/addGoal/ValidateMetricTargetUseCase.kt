package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.core.model.error.InputValidationError
import com.tbacademy.core.model.error.InputValidationResult
import javax.inject.Inject

interface ValidateMetricTargetUseCase {
    operator fun invoke(metricTarget : String) : InputValidationResult
}

class ValidateMetricTargetUseCaseImpl @Inject constructor() : ValidateMetricTargetUseCase{
    override fun invoke(metricTarget: String): InputValidationResult {
        return when {
            metricTarget.isBlank() -> InputValidationResult.Failure(InputValidationError.Empty)

            !metricTarget.matches(Regex("\\d+")) -> InputValidationResult.Failure(
                InputValidationError.Invalid)

            else -> {
                val value = metricTarget.toInt()
                when {
                    value <= 0 -> InputValidationResult.Failure(InputValidationError.Invalid)
                    else -> InputValidationResult.Success
                }
            }
        }
    }
}