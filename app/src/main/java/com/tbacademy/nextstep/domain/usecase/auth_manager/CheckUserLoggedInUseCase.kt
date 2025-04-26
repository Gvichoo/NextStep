package com.tbacademy.nextstep.domain.usecase.auth_manager

import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import javax.inject.Inject

interface CheckUserLoggedInUseCase {
    operator fun invoke(): Boolean
}

class CheckUserLoggedInUseCaseImpl @Inject constructor(
    private val authManager: AuthManager
) : CheckUserLoggedInUseCase {
    override fun invoke(): Boolean {
        return authManager.isUserLoggedIn()
    }
}