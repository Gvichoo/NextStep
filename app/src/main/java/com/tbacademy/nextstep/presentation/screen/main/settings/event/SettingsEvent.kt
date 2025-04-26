package com.tbacademy.nextstep.presentation.screen.main.settings.event

sealed interface SettingsEvent {
    data object Logout: SettingsEvent
    data class ChangeLanguage(val languageCode: String) : SettingsEvent
}