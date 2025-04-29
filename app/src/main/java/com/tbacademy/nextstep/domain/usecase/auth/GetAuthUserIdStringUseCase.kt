package com.tbacademy.nextstep.domain.usecase.auth

import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import javax.inject.Inject

interface GetAuthUserIdStringUseCase {
    operator fun invoke(): String?
}

class GetAuthUserIdStringUseCaseImpl @Inject constructor(
    private val authManager: AuthManager
) : GetAuthUserIdStringUseCase {
    override fun invoke(): String? {
        return authManager.getCurrentUserId()
    }
}