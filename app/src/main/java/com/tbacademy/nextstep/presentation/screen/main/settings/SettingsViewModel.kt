package com.tbacademy.nextstep.presentation.screen.main.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.core.model.onError
import com.tbacademy.core.model.onSuccess
import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.usecase.login.LogoutUserUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.SaveValueToPreferencesStorageUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import com.tbacademy.nextstep.presentation.screen.main.settings.event.SettingsEvent
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.state.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val saveValueUseCase: SaveValueToPreferencesStorageUseCase,
    private val readValueUseCase: ReadValueFromPreferencesStorageUseCase
) : BaseViewModel<SettingsState, SettingsEvent, SettingsEffect, Unit>(
    initialState = SettingsState(),
    initialUiState = Unit
) {

    init {
        restoreLocalSettings()
    }

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Logout -> logout()
            is SettingsEvent.ToggleThemeDropdown -> updateState {
                copy(isThemeDropdownExpanded = !isThemeDropdownExpanded)
            }

            is SettingsEvent.ToggleLanguageDropdown -> updateState {
                copy(isLanguageDropdownExpanded = !isLanguageDropdownExpanded)
            }

            is SettingsEvent.ThemeSelected -> selectTheme(theme = event.theme)

            is SettingsEvent.LanguageSelected -> selectLanguage(language = event.language)
        }
    }

    private fun selectTheme(theme: AppThemePresentation) {
        viewModelScope.launch {
            saveValueUseCase(AppPreferenceKeys.KEY_THEME_MODE, theme.name) // Save enum name
            updateState {
                copy(selectedTheme = theme, isThemeDropdownExpanded = false)
            }
        }
    }

    private fun selectLanguage(language: AppLanguagePresentation) {
        viewModelScope.launch {
            saveValueUseCase(AppPreferenceKeys.LANGUAGE_KEY, language.name) // Save enum name
            updateState {
                copy(selectedLanguage = language, isLanguageDropdownExpanded = false)
            }
            emitEffect(SettingsEffect.ApplyLanguage(language))
        }
    }


    // On Logout
    private fun logout() {
        viewModelScope.launch {
            logoutUserUseCase().collect { resource ->
                resource.onSuccess {
                    emitEffect(effect = SettingsEffect.NavigateToLogin)
                }

                resource.onError {
                    Log.d("LOG_OUT_ERROR", "Error")
                    emitEffect(effect = SettingsEffect.NavigateToLogin)
                }
            }
        }
    }

    private fun restoreLocalSettings() {
        viewModelScope.launch {
            readValueUseCase(AppPreferenceKeys.LANGUAGE_KEY).collect { savedLanguage ->
                val language = savedLanguage?.let { saved ->
                    AppLanguagePresentation.valueOf(saved)
                } ?: AppLanguagePresentation.SYSTEM

                updateState { copy(selectedLanguage = language) }
            }
        }

        viewModelScope.launch {
            readValueUseCase(AppPreferenceKeys.KEY_THEME_MODE).collect { savedTheme ->
                val theme = savedTheme?.let { saved ->
                    AppThemePresentation.valueOf(saved)
                } ?: AppThemePresentation.SYSTEM

                updateState { copy(selectedTheme = theme) }
            }
        }
    }
}