package com.tbacademy.nextstep.domain.usecase.validation.authorization

import com.tbacademy.core.InputValidationError
import com.tbacademy.core.InputValidationResult
import javax.inject.Inject

interface ValidateUsernameUseCase {
    operator fun invoke(username: String): InputValidationResult
}

class ValidateUsernameUseCaseImpl @Inject constructor(): ValidateUsernameUseCase {

    override operator fun invoke(username: String): InputValidationResult {

        return when {
            username.isEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            username.length < 4 -> InputValidationResult.Failure(error = InputValidationError.UsernameShort)
            username.length > 20 -> InputValidationResult.Failure(error = InputValidationError.UsernameLong)
            !username.matches(("^[A-Za-z0-9_]+$".toRegex())) -> {
                InputValidationResult.Failure(error = InputValidationError.InvalidUsernameFormat)
            }

            else -> {
                InputValidationResult.Success
            }
        }
    }
}