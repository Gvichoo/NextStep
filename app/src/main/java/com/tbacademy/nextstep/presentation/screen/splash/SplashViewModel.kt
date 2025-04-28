package com.tbacademy.nextstep.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbacademy.core.model.onSuccess
import com.tbacademy.nextstep.domain.usecase.auth_manager.CheckUserLoggedInUseCase
import com.tbacademy.nextstep.domain.usecase.login.LogoutUserUseCase
import com.tbacademy.nextstep.domain.usecase.user_session.GetUserSessionUseCase
import com.tbacademy.nextstep.presentation.screen.splash.effect.SplashEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase,
    private val logoutUseCase: LogoutUserUseCase
) : ViewModel() {

    private val _effect = Channel<SplashEffect>()
    val effect: Flow<SplashEffect> get() = _effect.receiveAsFlow()

    init {
        readSession()
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
}