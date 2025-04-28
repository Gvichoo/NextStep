package com.tbacademy.nextstep.presentation.common.mapper

import com.tbacademy.nextstep.R
import com.tbacademy.core.model.error.ApiError

fun ApiError.toMessageRes(): Int {
    return when (this) {
        ApiError.Network, ApiError.Timeout -> R.string.error_network
        ApiError.Unauthorized -> R.string.error_unauthorized
        ApiError.Forbidden, ApiError.Conflict -> R.string.error_forbidden
        ApiError.NotFound -> R.string.error_not_found
        ApiError.InternalServer -> R.string.error_server
        ApiError.ServiceUnavailable -> R.string.error_service_unavailable
        ApiError.UserNotFound -> R.string.error_user_not_found
        ApiError.InvalidCredentials -> R.string.error_invalid_credentials
        ApiError.UserAlreadyExists -> R.string.error_user_already_exists
        else -> R.string.error_unknown
    }
}