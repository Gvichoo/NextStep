package com.tbacademy.nextstep.presentation.screen.main.settings

import com.tbacademy.core.model.settings.AppLanguage
import com.tbacademy.core.model.settings.AppTheme

// Convert String from dropdown to AppTheme
fun String.toAppTheme(): AppTheme {
    return when (this.lowercase()) {
        "light" -> AppTheme.LIGHT
        "dark" -> AppTheme.DARK
        else -> AppTheme.SYSTEM
    }
}

// Convert AppTheme to readable String
fun AppTheme.toDisplayString(): String {
    return when (this) {
        AppTheme.SYSTEM -> "System"
        AppTheme.LIGHT -> "Light"
        AppTheme.DARK -> "Dark"
    }
}

// Convert String from dropdown to AppLanguage
fun String.toAppLanguage(): AppLanguage {
    return when (this.lowercase()) {
        "english" -> AppLanguage.EN
        "georgian" -> AppLanguage.KA
        else -> AppLanguage.SYSTEM
    }
}

// Convert AppLanguage to readable String
fun AppLanguage.toDisplayString(): String {
    return when (this) {
        AppLanguage.SYSTEM -> "System"
        AppLanguage.EN -> "English"
        AppLanguage.KA -> "Georgian"
    }
}