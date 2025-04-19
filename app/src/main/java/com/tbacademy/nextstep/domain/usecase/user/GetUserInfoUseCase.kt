package com.tbacademy.nextstep.domain.usecase.user

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.User
import com.tbacademy.nextstep.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetUserInfoUseCase {
    suspend operator fun invoke(userId: String? = null): Flow<Resource<User>>
}

class GetUserInfoUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
): GetUserInfoUseCase {
    override suspend fun invoke(userId: String?): Flow<Resource<User>> {
        return userRepository.getUserInfo(userId = userId)
    }
}