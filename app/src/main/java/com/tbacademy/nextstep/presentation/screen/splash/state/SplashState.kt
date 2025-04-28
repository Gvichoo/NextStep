package com.tbacademy.nextstep.presentation.screen.splash.state

import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation

data class SplashState(
    val theme: AppThemePresentation = AppThemePresentation.SYSTEM,
    val language: AppLanguagePresentation = AppLanguagePresentation.SYSTEM,
    val isReady: Boolean = false
)