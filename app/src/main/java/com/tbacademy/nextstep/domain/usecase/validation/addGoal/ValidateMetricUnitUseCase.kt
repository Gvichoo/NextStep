package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.nextstep.data.enumClass.MetricUnit
import com.tbacademy.core.InputValidationError
import com.tbacademy.core.InputValidationResult
import javax.inject.Inject

interface ValidateMetricUnitUseCase {
    operator fun invoke(metricUnit: String): InputValidationResult
}

class ValidateMetricUnitUseCaseImpl @Inject constructor() : ValidateMetricUnitUseCase {
    override fun invoke(metricUnit: String): InputValidationResult {
        return when {
            metricUnit.isBlank() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            MetricUnit.from(metricUnit) == null -> InputValidationResult.Failure(error = InputValidationError.Invalid)
            else -> InputValidationResult.Success
        }
    }
}