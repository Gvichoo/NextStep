package com.tbacademy.nextstep.data.httpHelper

import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.core.model.error.ApiError
import com.tbacademy.core.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class ApiHelper {
    suspend fun <T> handleHttpRequest(
        apiCall: suspend () -> Response<T>
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading(loading = true))
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(data = it))
                } ?: emit(Resource.Error(error = ApiError.Unknown()))
            } else {
                val apiError = when (val code = response.code()) {
                    401 -> ApiError.Unauthorized
                    403 -> ApiError.Forbidden
                    404 -> ApiError.NotFound
                    409 -> ApiError.Conflict
                    500 -> ApiError.InternalServer
                    503 -> ApiError.ServiceUnavailable
                    504 -> ApiError.Timeout
                    else -> ApiError.Unknown(message = "HTTP ${code}: ${response.message()}")
                }
                emit(Resource.Error(error = apiError))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Error(throwable.toApiError()))
        } finally {
            emit(Resource.Loading(loading = false))
        }
    }
}