
package com.tbacademy.nextstep.presentation.screen.main.settings.effect

sealed interface SettingsEffect {
    data object NavigateToLogin: SettingsEffect
}
