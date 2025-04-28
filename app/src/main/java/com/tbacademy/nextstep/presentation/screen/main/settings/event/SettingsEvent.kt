package com.tbacademy.nextstep.presentation.screen.main.settings.event

import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation

sealed interface SettingsEvent {
    data object ToggleThemeDropdown : SettingsEvent
    data object ToggleLanguageDropdown : SettingsEvent
    data class ThemeSelected(val theme: AppThemePresentation) : SettingsEvent
    data class LanguageSelected(val language: AppLanguagePresentation) : SettingsEvent
    data object Logout: SettingsEvent
}