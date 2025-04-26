package com.tbacademy.nextstep.data.common.mapper

import android.content.res.Resources.NotFoundException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.tbacademy.nextstep.domain.core.ApiError
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toApiError(): ApiError = when (this) {
    is IOException -> ApiError.Network
    is NotFoundException -> ApiError.NotFound
    is HttpException -> this.toApiError()
    is IllegalStateException -> ApiError.Custom(message ?: "Illegal state")
    is FirebaseAuthInvalidUserException -> ApiError.UserNotFound
    is FirebaseNoSignedInUserException -> ApiError.UserNotFound
    is FirebaseAuthInvalidCredentialsException -> ApiError.InvalidCredentials
    is FirebaseAuthUserCollisionException -> ApiError.UserAlreadyExists
    else -> ApiError.Custom(message ?: "Unexpected error")
}

fun HttpException.toApiError(): ApiError = when (this.code()) {
    401 -> ApiError.Unauthorized
    403 -> ApiError.Forbidden
    404 -> ApiError.NotFound
    409 -> ApiError.Conflict
    500 -> ApiError.InternalServer
    503 -> ApiError.ServiceUnavailable
    504 -> ApiError.Timeout
    else -> ApiError.Unknown()
}