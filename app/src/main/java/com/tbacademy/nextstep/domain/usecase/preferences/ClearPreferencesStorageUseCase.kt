package com.tbacademy.nextstep.domain.usecase.preferences

import com.tbacademy.nextstep.domain.manager.preferences.PreferencesManager
import javax.inject.Inject

interface ClearPreferencesStorageUseCase {
    suspend operator fun invoke()
}

class ClearPreferencesStorageUseCaseImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ClearPreferencesStorageUseCase {
    override suspend fun invoke() {
        return preferencesManager.clear()
    }

}