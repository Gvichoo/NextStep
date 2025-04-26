package com.tbacademy.nextstep.domain.usecase.preferences

import com.tbacademy.nextstep.domain.manager.preferences.PreferenceKey
import com.tbacademy.nextstep.domain.manager.preferences.PreferencesManager
import javax.inject.Inject

interface SaveValueToPreferencesStorageUseCase {
    suspend operator fun <T> invoke(key: PreferenceKey<T>, value : T)
}

class SaveValueToPreferencesStorageUseCaseImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : SaveValueToPreferencesStorageUseCase {
    override suspend operator fun <T> invoke(key: PreferenceKey<T>, value : T) {
        return preferencesManager.saveValue(key,value)
    }
}