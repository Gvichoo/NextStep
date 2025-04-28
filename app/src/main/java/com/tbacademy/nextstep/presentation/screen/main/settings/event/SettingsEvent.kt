package com.tbacademy.nextstep.presentation.screen.main.settings.event

import com.tbacademy.core.model.settings.AppLanguage
import com.tbacademy.core.model.settings.AppTheme

sealed interface SettingsEvent {
    data object ToggleThemeDropdown : SettingsEvent
    data object ToggleLanguageDropdown : SettingsEvent
    data class ThemeSelected(val theme: AppTheme) : SettingsEvent
    data class LanguageSelected(val language: AppLanguage) : SettingsEvent
    data object Logout: SettingsEvent
}