package com.tbacademy.nextstep.domain.usecase.preferences

import com.tbacademy.nextstep.domain.manager.preferences.PreferencesManager
import javax.inject.Inject

interface ClearValueFromPreferencesStorageUseCase {
    suspend operator fun invoke()
}

class ClearValueFromLocalStorageUseCaseImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ClearValueFromPreferencesStorageUseCase {
    override suspend fun invoke() {
        return preferencesManager.clear()
    }

}