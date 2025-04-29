package com.tbacademy.nextstep.domain.usecase.theme

import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.SaveValueToPreferencesStorageUseCase
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

interface AutoSwitchDarkThemeIfBatteryLowUseCase {
    suspend operator fun invoke(): Boolean
}

class AutoSwitchDarkThemeIfBatteryLowUseCaseImpl @Inject constructor(
    private val readPrefs: ReadValueFromPreferencesStorageUseCase,
    private val savePrefs: SaveValueToPreferencesStorageUseCase
) : AutoSwitchDarkThemeIfBatteryLowUseCase {

    override suspend fun invoke(): Boolean {
        val currentTheme = readPrefs(AppPreferenceKeys.KEY_THEME_MODE).firstOrNull()

        return if (
            currentTheme == null ||
            currentTheme == AppThemePresentation.LIGHT.name ||
            currentTheme == AppThemePresentation.SYSTEM.name
        ) {
            savePrefs(AppPreferenceKeys.KEY_THEME_MODE, AppThemePresentation.DARK.name)
            true
        } else {
            false
        }
    }
}