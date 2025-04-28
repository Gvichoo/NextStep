package com.tbacademy.nextstep.domain.usecase.validation.authorization

import com.tbacademy.core.model.error.InputValidationError
import com.tbacademy.core.model.error.InputValidationResult
import javax.inject.Inject

interface ValidateRepeatedPasswordUseCase {
    operator fun invoke(password: String, repeatedPassword: String): InputValidationResult
}

class ValidateRepeatedPasswordUseCaseImpl @Inject constructor(): ValidateRepeatedPasswordUseCase {

    override operator fun invoke(password: String, repeatedPassword: String): InputValidationResult {

        return when{
            repeatedPassword.isEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            password != repeatedPassword -> InputValidationResult.Failure(error = InputValidationError.PasswordsDoNotMatch)
            else -> InputValidationResult.Success
        }
    }
}