package com.tbacademy.nextstep.presentation.screen.main.settings.effect

sealed interface SettingsEffect {
    data object ShowSuccessMessage : SettingsEffect
    data class ShowErrorMessage(val messageRes: Int) : SettingsEffect
    data object NavigateToLogin: SettingsEffect
}