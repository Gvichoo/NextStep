package com.tbacademy.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: ApiError) : Resource<Nothing>()
    data class Loading(val loading: Boolean) : Resource<Nothing>()
}

fun <T> Flow<Resource<T>>.handleResource(): Flow<Resource<T>> {
    return this.map { resource ->
        when (resource) {
            is Resource.Loading -> com.tbacademy.core.Resource.Loading(resource.loading)
            is Resource.Error -> com.tbacademy.core.Resource.Error(resource.error)
            is Resource.Success -> com.tbacademy.core.Resource.Success(resource.data)
        }
    }
}

fun <T> Flow<Resource<T>>.handleSuccess(onSuccess: suspend () -> Unit): Flow<Resource<T>> {
    return this.map { resource ->
        when (resource) {
            is Resource.Loading -> com.tbacademy.core.Resource.Loading(resource.loading)
            is Resource.Error -> com.tbacademy.core.Resource.Error(resource.error)
            is Resource.Success -> {
                onSuccess()
                com.tbacademy.core.Resource.Success(resource.data)
            }
        }
    }
}

fun <DTO, DOMAIN> Flow<Resource<DTO>>.mapResource(mapper: (DTO) -> DOMAIN): Flow<Resource<DOMAIN>> {
    return this.map { resource ->
        when (resource) {
            is Resource.Loading -> com.tbacademy.core.Resource.Loading(resource.loading)
            is Resource.Error -> com.tbacademy.core.Resource.Error(resource.error)
            is Resource.Success -> {
                com.tbacademy.core.Resource.Success(mapper(resource.data))
            }
        }
    }
}

inline fun <T> Resource<T>.onError(action: (ApiError) -> Unit): Resource<T> {
    if (this is Resource.Error) {
        action(error)
    }
    return this
}

inline fun <T> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) {
        action(data)
    }
    return this
}