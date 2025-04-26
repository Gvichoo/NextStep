package com.tbacademy.nextstep.domain.usecase.preferences

import com.tbacademy.nextstep.domain.manager.preferences.PreferenceKey
import com.tbacademy.nextstep.domain.manager.preferences.PreferencesManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ReadValueFromPreferencesStorageUseCase {
    operator fun <T> invoke(key: PreferenceKey<T>): Flow<T?>
}

class ReadValueFromPreferencesStorageUseCaseImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ReadValueFromPreferencesStorageUseCase {
    override operator fun <T> invoke(key: PreferenceKey<T>): Flow<T?> {
        return preferencesManager.readValue(key = key)
    }
}