package com.tbacademy.nextstep.presentation.screen.main.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.onError
import com.tbacademy.nextstep.domain.core.onSuccess
import com.tbacademy.nextstep.domain.usecase.login.LogoutUserUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import com.tbacademy.nextstep.presentation.screen.main.settings.event.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase
) : BaseViewModel<Unit, SettingsEvent, SettingsEffect, Unit>(
    initialState = Unit,
    initialUiState = Unit
) {

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Logout -> logout()
        }
    }

    // On Logout
    private fun logout() {
        viewModelScope.launch {
            logoutUserUseCase().collect { resource ->
                resource.onSuccess {
                    emitEffect(effect = SettingsEffect.NavigateToLogin)
                }

                resource.onError {
                    Log.d("LOG_OUT_ERROR", "Error")
                    emitEffect(effect = SettingsEffect.NavigateToLogin)
                }
            }
        }
    }
}