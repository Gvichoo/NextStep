package com.tbacademy.nextstep.domain.usecase.validation.authorization

import com.tbacademy.core.InputValidationError
import com.tbacademy.core.InputValidationResult
import java.util.regex.Pattern.compile
import javax.inject.Inject

interface ValidateEmailUseCase {
    operator fun invoke(email: String?): InputValidationResult
}

class ValidateEmailUseCaseImpl @Inject constructor(): ValidateEmailUseCase {
    private val emailRegex = compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override operator fun invoke(email: String?): InputValidationResult {
        return when {
            email.isNullOrEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            !emailRegex.matcher(email).matches() -> InputValidationResult.Failure(error = InputValidationError.InvalidEmailFormat)
            else -> InputValidationResult.Success
        }
    }
}