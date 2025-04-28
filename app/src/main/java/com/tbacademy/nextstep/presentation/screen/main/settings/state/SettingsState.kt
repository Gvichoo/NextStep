
package com.tbacademy.nextstep.presentation.screen.main.settings.state

import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation

data class SettingsState(
    val appThemeOptions: List<AppThemePresentation> = AppThemePresentation.entries,
    val appLanguageOptions: List<AppLanguagePresentation> = AppLanguagePresentation.entries,
    val selectedTheme: AppThemePresentation = AppThemePresentation.SYSTEM,
    val selectedLanguage: AppLanguagePresentation = AppLanguagePresentation.SYSTEM,
    val isThemeDropdownExpanded: Boolean = false,
    val isLanguageDropdownExpanded: Boolean = false,
    val isLoading: Boolean = false
)