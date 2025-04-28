package com.tbacademy.nextstep.presentation.screen.main.settings.state

import com.tbacademy.core.model.settings.AppLanguage
import com.tbacademy.core.model.settings.AppTheme

data class SettingsState(
    val selectedTheme: AppTheme = AppTheme.SYSTEM,
    val isThemeDropdownExpanded: Boolean = false,
    val selectedLanguage: AppLanguage = AppLanguage.SYSTEM,
    val isLanguageDropdownExpanded: Boolean = false,
    val isLoading: Boolean = false
)