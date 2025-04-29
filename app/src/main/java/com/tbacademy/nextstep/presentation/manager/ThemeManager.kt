package com.tbacademy.nextstep.presentation.manager

import androidx.appcompat.app.AppCompatDelegate
import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    private val readPrefs: ReadValueFromPreferencesStorageUseCase
) {
    fun observeAndApplyTheme() {
        CoroutineScope(Dispatchers.Main).launch {
            readPrefs(AppPreferenceKeys.KEY_THEME_MODE)
                .filterNotNull()
                .collectLatest { savedTheme ->
                    when (AppThemePresentation.valueOf(savedTheme)) {
                        AppThemePresentation.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        AppThemePresentation.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        AppThemePresentation.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
        }
    }
}