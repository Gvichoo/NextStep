package com.tbacademy.core

sealed interface ApiError {
    data object Network : ApiError
    data object Unauthorized : ApiError
    data object Forbidden : ApiError
    data object NotFound : ApiError
    data object Conflict : ApiError
    data object InternalServer : ApiError
    data object ServiceUnavailable : ApiError
    data object Timeout : ApiError
    data object UserNotFound : ApiError
    data object InvalidCredentials : ApiError
    data object UserAlreadyExists : ApiError
    data class Unknown(val message: String? = null) : ApiError
    data class Custom(val message: String) : ApiError
}