package com.tbacademy.nextstep.presentation.screen.main.settings.model

import com.tbacademy.core.model.settings.AppLanguage
import com.tbacademy.nextstep.R

enum class AppLanguagePresentation(val titleRes: Int) {
    SYSTEM(titleRes = R.string.system),
    EN(titleRes = R.string.english),
    KA(titleRes = R.string.georgian)
}

fun AppLanguage.toPresentation(): AppLanguagePresentation {
    return when (this) {
        AppLanguage.SYSTEM -> AppLanguagePresentation.SYSTEM
        AppLanguage.EN -> AppLanguagePresentation.EN
        AppLanguage.KA -> AppLanguagePresentation.KA
    }
}

fun AppLanguagePresentation.toDomain(): AppLanguage {
    return when (this) {
        AppLanguagePresentation.SYSTEM -> AppLanguage.SYSTEM
        AppLanguagePresentation.EN -> AppLanguage.EN
        AppLanguagePresentation.KA -> AppLanguage.KA
    }
}