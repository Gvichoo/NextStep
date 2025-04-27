package com.tbacademy.nextstep.presentation.common.mapper

import com.tbacademy.nextstep.R
import com.tbacademy.core.InputValidationError

fun InputValidationError.toErrorMessageRes(): Int {
    return when (this) {
        InputValidationError.Empty -> R.string.field_is_empty
        InputValidationError.InvalidEmailFormat -> R.string.invalid_email_format
        InputValidationError.UsernameShort -> R.string.username_must_be_at_least_4_characters
        InputValidationError.UsernameLong -> R.string.nickname_must_be_no_longer_than_20_characters
        InputValidationError.InvalidUsernameFormat -> R.string.nickname_can_only_contain_letters_numbers_and_underscores
        InputValidationError.PasswordShort -> R.string.password_should_be_at_least_8_characters
        InputValidationError.PasswordWeak -> R.string.password_must_contain_at_least_one_letter_and_one_digit
        InputValidationError.PasswordsDoNotMatch -> R.string.passwords_do_not_match
        InputValidationError.Invalid -> R.string.invalid_format
    }
}