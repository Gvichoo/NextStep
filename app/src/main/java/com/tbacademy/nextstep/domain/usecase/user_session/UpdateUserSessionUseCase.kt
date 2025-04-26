package com.tbacademy.nextstep.domain.usecase.user_session

import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.usecase.preferences.SaveValueToPreferencesStorageUseCase
import javax.inject.Inject

interface UpdateUserSessionUseCase {
    suspend operator fun invoke(rememberMe: Boolean)
}

class UpdateUserSessionUseCaseImpl @Inject constructor(
    private val saveValueToPreferencesStorageUseCase: SaveValueToPreferencesStorageUseCase
) : UpdateUserSessionUseCase {
    override suspend fun invoke(rememberMe: Boolean) {
        saveValueToPreferencesStorageUseCase(key = AppPreferenceKeys.KEY_REMEMBER_ME, value = rememberMe)
    }
}