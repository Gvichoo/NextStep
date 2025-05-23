package com.tbacademy.nextstep.domain.usecase.auth

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetAuthUserIdFlowUseCase {
    suspend operator fun invoke(): Flow<Resource<String>>
}

class GetAuthUserIdFlowUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
): GetAuthUserIdFlowUseCase {
    override suspend fun invoke(): Flow<Resource<String>> {
        return authRepository.getCurrentUserId()
    }
}