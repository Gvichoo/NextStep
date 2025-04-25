package com.tbacademy.nextstep.presentation.screen.main.notification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.notification.MarkAllNotificationsAsReadUseCase

import com.tbacademy.nextstep.domain.usecase.notification.GetUserNotificationsUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.screen.main.notification.effect.NotificationEffect
import com.tbacademy.nextstep.presentation.screen.main.notification.event.NotificationEvent
import com.tbacademy.nextstep.presentation.screen.main.notification.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.notification.state.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getUserNotificationsUseCase: GetUserNotificationsUseCase,
    private val markAllNotificationsAsReadUseCase: MarkAllNotificationsAsReadUseCase
) : BaseViewModel<NotificationState, NotificationEvent, NotificationEffect, Unit>
    (initialState = NotificationState(), initialUiState = Unit) {
    override fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.GetNotifications -> getUserNotifications()
        }
    }

    private fun getUserNotifications() {
        viewModelScope.launch {
            getUserNotificationsUseCase().collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        updateState {
                            copy(notifications = resource.data.map { it.toPresentation() })
                        }
                        markAllAsRead()
                    }

                    is Resource.Error -> {
                        Log.d("NOTIFICATION_ERROR", "${resource.error}")
                        emitEffect(effect = NotificationEffect.ShowErrorMessage(errorMessageRes = resource.error.toMessageRes()))
                        updateState { copy(errorMessageRes = resource.error.toMessageRes()) }
                    }

                    is Resource.Loading -> updateState { copy(isLoading = resource.loading) }
                }
            }
        }
    }

    private fun markAllAsRead() {
        viewModelScope.launch {
            markAllNotificationsAsReadUseCase()
        }
    }
}