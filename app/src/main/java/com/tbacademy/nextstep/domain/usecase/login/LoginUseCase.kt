package com.tbacademy.nextstep.domain.usecase.login

import com.tbacademy.core.Resource
import com.tbacademy.core.handleSuccess
import com.tbacademy.nextstep.domain.repository.login.LoginRepository
import com.tbacademy.nextstep.domain.usecase.user_session.UpdateUserSessionUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val updateUserSessionUseCase: UpdateUserSessionUseCase
) {
    operator fun invoke(email: String, password: String, rememberMe: Boolean): Flow<Resource<Boolean>> {
        return loginRepository.login(email, password).handleSuccess {
            updateUserSessionUseCase(rememberMe = rememberMe)
        }
    }
}