package com.tbacademy.nextstep.presentation.screen.main.main_screen

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.usecase.notification.ListenForUnreadNotificationsUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.main_screen.event.MainEvent
import com.tbacademy.nextstep.presentation.screen.main.main_screen.state.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val listenForUnreadNotificationsUseCase: ListenForUnreadNotificationsUseCase
) : BaseViewModel<MainState, MainEvent, Unit>(
    initialState = MainState()
) {
    override fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.StartListeningForUnreadNotifications -> startListeningForUnreadNotifications()
        }
    }

    private fun startListeningForUnreadNotifications() {
        viewModelScope.launch {
            listenForUnreadNotificationsUseCase().collect { hasUnread ->
                updateState { copy(hasUnreadNotifications = hasUnread) }
            }
        }
    }
}