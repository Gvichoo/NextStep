package com.tbacademy.nextstep.presentation.screen.main.settings.model

import com.tbacademy.core.model.settings.AppTheme
import com.tbacademy.nextstep.R

enum class AppThemePresentation(val titleRes: Int) {
    SYSTEM(titleRes = R.string.system),
    LIGHT(titleRes = R.string.light),
    DARK(titleRes = R.string.dark)
}

fun AppTheme.toPresentation(): AppThemePresentation {
    return when (this) {
        AppTheme.SYSTEM -> AppThemePresentation.SYSTEM
        AppTheme.LIGHT -> AppThemePresentation.LIGHT
        AppTheme.DARK -> AppThemePresentation.DARK
    }
}

fun AppThemePresentation.toDomainTheme(): AppTheme {
    return when (this) {
        AppThemePresentation.SYSTEM -> AppTheme.SYSTEM
        AppThemePresentation.LIGHT -> AppTheme.LIGHT
        AppThemePresentation.DARK -> AppTheme.DARK
    }
}