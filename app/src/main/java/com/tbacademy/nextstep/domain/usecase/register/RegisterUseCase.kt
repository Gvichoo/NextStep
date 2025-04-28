package com.tbacademy.nextstep.domain.usecase.register

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.repository.register.RegisterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    operator fun invoke(nickname: String, email: String, password: String): Flow<Resource<Boolean>> {
        return registerRepository.register(nickname, email, password)
    }
}