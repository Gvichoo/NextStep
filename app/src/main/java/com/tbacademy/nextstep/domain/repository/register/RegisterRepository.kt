package com.tbacademy.nextstep.domain.repository.register

import com.tbacademy.nextstep.domain.core.Resource
import kotlinx.coroutines.flow.Flow

interface RegisterRepository {
    fun register(nickname: String, email: String, password: String): Flow<Resource<Boolean>>
}