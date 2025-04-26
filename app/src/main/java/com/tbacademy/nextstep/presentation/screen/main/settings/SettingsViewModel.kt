package com.tbacademy.nextstep.presentation.screen.main.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.onError
import com.tbacademy.nextstep.domain.core.onSuccess
import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.manager.preferences.PreferenceKey
import com.tbacademy.nextstep.domain.usecase.login.LogoutUserUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.SaveValueToPreferencesStorageUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import com.tbacademy.nextstep.presentation.screen.main.settings.event.SettingsEvent
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
        // Read saved language when ViewModel is created
        viewModelScope.launch {
            readValueUseCase(AppPreferenceKeys.LANGUAGE_KEY).collect { language ->
                // If language is null, default to "en"
                val currentLanguage = language ?: "en"
                updateState { copy(language = currentLanguage) }
            }
        }
    }

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Logout -> logout()
            is SettingsEvent.ChangeLanguage -> changeLanguage(event.languageCode)
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

    private fun changeLanguage(languageCode: String) {
        viewModelScope.launch {
            // Save the new language to preferences using the use case
            saveValueUseCase(AppPreferenceKeys.LANGUAGE_KEY, languageCode)

            // Update the UI state with the new language
            updateState { copy(language = languageCode) }
        }
    }

}