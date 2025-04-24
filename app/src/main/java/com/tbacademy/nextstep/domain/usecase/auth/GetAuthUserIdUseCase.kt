package com.tbacademy.nextstep.domain.usecase.auth

import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import javax.inject.Inject

interface GetAuthUserIdUseCase {
    suspend operator fun invoke(): String?
}

class GetAuthUserIdUseCaseImpl @Inject constructor(
    private val authManager: AuthManager
): GetAuthUserIdUseCase {
    override suspend fun invoke(): String? {
        return authManager.getCurrentUserId()
    }
}