package com.tbacademy.nextstep.domain.usecase.preferences

import com.tbacademy.nextstep.domain.manager.preferences.PreferenceKey
import com.tbacademy.nextstep.domain.manager.preferences.PreferencesManager
import javax.inject.Inject

interface ClearValueFromPreferencesStorageUseCase {
    suspend operator fun <T> invoke(key: PreferenceKey<T>)
}

class ClearValueFromPreferencesStorageUseCaseImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ClearValueFromPreferencesStorageUseCase {
    override suspend fun <T> invoke(key: PreferenceKey<T>) {
        preferencesManager.clearByKey(key = key)
    }
}