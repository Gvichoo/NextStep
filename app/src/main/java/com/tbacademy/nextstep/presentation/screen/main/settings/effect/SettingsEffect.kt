
package com.tbacademy.nextstep.presentation.screen.main.settings.effect

import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation

sealed interface SettingsEffect {
    data object ShowSuccessMessage : SettingsEffect
    data class ShowErrorMessage(val messageRes: Int) : SettingsEffect
    data class ApplyTheme(val theme: AppThemePresentation): SettingsEffect
    data class ApplyLanguage(val language: AppLanguagePresentation): SettingsEffect
    data object NavigateToLogin: SettingsEffect
}
