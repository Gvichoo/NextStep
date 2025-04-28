package com.tbacademy.nextstep.domain.repository.auth

import com.tbacademy.core.model.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUserId(): Flow<Resource<String>>
}
