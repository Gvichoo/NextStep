package com.tbacademy.nextstep.domain.usecase.user_session

import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface GetUserSessionUseCase {
    suspend operator fun invoke(): Boolean
}

class GetUserSessionUseCaseImpl @Inject constructor(
    private val readValueFromPreferencesStorageUseCase: ReadValueFromPreferencesStorageUseCase
) : GetUserSessionUseCase {
    override suspend fun invoke(): Boolean {
        return readValueFromPreferencesStorageUseCase(key = AppPreferenceKeys.KEY_REMEMBER_ME)
            .first() ?: false
    }
}