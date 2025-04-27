package com.tbacademy.nextstep.domain.usecase.login

import com.tbacademy.core.Resource
import com.tbacademy.core.handleSuccess
import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.repository.login.LoginRepository
import com.tbacademy.nextstep.domain.usecase.preferences.ClearValueFromPreferencesStorageUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LogoutUserUseCase {
    operator fun invoke(): Flow<Resource<Unit>>
}

class LogoutUserUseCaseImpl @Inject constructor(
    private val loginRepository: LoginRepository,
    private val clearValueFromPreferencesStorageUseCase: ClearValueFromPreferencesStorageUseCase
) : LogoutUserUseCase {
    override fun invoke(): Flow<Resource<Unit>> {
        return loginRepository.logout().handleSuccess {
            clearValueFromPreferencesStorageUseCase(key = AppPreferenceKeys.KEY_REMEMBER_ME)
        }
    }
}