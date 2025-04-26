package com.tbacademy.nextstep.domain.repository.login

import com.tbacademy.nextstep.domain.core.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun login(email: String, password: String): Flow<Resource<Boolean>>
    fun logout(): Flow<Resource<Unit>>
}