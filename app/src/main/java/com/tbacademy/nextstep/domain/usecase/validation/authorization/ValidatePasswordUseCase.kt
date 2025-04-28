package com.tbacademy.nextstep.domain.usecase.validation.authorization

import com.tbacademy.core.model.error.InputValidationError
import com.tbacademy.core.model.error.InputValidationResult
import java.util.regex.Pattern
import javax.inject.Inject

interface ValidatePasswordUseCase {
    operator fun invoke(password: String): InputValidationResult
}

class ValidatePasswordUseCaseImpl @Inject constructor() : ValidatePasswordUseCase {
    private val passwordRegex = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).+$")

    override operator fun invoke(password: String): InputValidationResult {
        return when {
            password.isEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            password.length < 8 -> InputValidationResult.Failure(error = InputValidationError.PasswordShort)
            !passwordRegex.matcher(password)
                .matches() -> InputValidationResult.Failure(error = InputValidationError.PasswordWeak)

            else -> InputValidationResult.Success
        }
    }
}