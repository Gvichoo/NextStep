package com.tbacademy.nextstep.domain.repository.user

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserInfo(userId: String): Flow<Resource<User>>
}