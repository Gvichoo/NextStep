package com.tbacademy.nextstep.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbacademy.core.model.onSuccess
import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.usecase.auth_manager.CheckUserLoggedInUseCase
import com.tbacademy.nextstep.domain.usecase.login.LogoutUserUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import com.tbacademy.nextstep.domain.usecase.user_session.GetUserSessionUseCase
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation
import com.tbacademy.nextstep.presentation.screen.splash.effect.SplashEffect
import com.tbacademy.nextstep.presentation.screen.splash.state.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase,
    private val readValueUseCase: ReadValueFromPreferencesStorageUseCase,
    private val logoutUseCase: LogoutUserUseCase
) : ViewModel() {

    private val _effect = Channel<SplashEffect>()
    val effect: Flow<SplashEffect> get() = _effect.receiveAsFlow()

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state

    init {
        viewModelScope.launch {
            loadPreferences()
            readSession()
        }
    }

    private fun readSession() {
        viewModelScope.launch {
            val rememberMe: Boolean = getUserSessionUseCase()
            val isLoggedIn = checkUserLoggedInUseCase()

            if (!rememberMe && isLoggedIn) {
                logoutUseCase().collect { resource ->
                    resource.onSuccess {
                        _effect.send(SplashEffect.NavigateToLogin)
                    }
                }
            } else if (isLoggedIn) {
                _effect.send(SplashEffect.NavigateToMain)
            } else {
                _effect.send(SplashEffect.NavigateToLogin)
            }
        }
    }


    private fun loadPreferences() {
        viewModelScope.launch {
            val savedTheme = readValueUseCase(AppPreferenceKeys.KEY_THEME_MODE).firstOrNull()
            val savedLanguage = readValueUseCase(AppPreferenceKeys.LANGUAGE_KEY).firstOrNull()

            _state.value = SplashState(
                theme = savedTheme?.let { AppThemePresentation.valueOf(it) } ?: AppThemePresentation.SYSTEM,
                language = savedLanguage?.let { AppLanguagePresentation.valueOf(it) } ?: AppLanguagePresentation.SYSTEM,
                isReady = true
            )
        }
    }
}