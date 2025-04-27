package com.tbacademy.nextstep.domain.repository.auth

import com.tbacademy.core.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUserId(): Flow<Resource<String>>
}
