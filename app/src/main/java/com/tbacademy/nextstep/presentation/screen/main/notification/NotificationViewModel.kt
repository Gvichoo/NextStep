package com.tbacademy.nextstep.presentation.screen.main.notification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.notification.GetUserNotificationsUseCase
import com.tbacademy.nextstep.domain.usecase.notification.MarkAllNotificationsAsReadUseCase
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
            is NotificationEvent.GetNotifications -> getUserNotifications(isRefresh = event.refresh)
            is NotificationEvent.PostNotificationSelected -> onReactNotificationSelected(
                postId = event.postId,
                isComment = event.isComment
            )
            is NotificationEvent.FollowNotificationSelected -> onFollowNotificationSelected(userId = event.userId)
        }
    }

    private fun onReactNotificationSelected(postId: String, isComment: Boolean = false) {
        viewModelScope.launch {
            emitEffect(
                effect = NotificationEffect.NavigateToPost(
                    postId = postId,
                    isComment = isComment
                )
            )
        }
    }

    private fun onFollowNotificationSelected(userId: String) {
        viewModelScope.launch {
            emitEffect(effect = NotificationEffect.NavigateToUserProfile(userId = userId))
        }
    }

    private fun getUserNotifications(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                updateState { copy(isRefreshing = true) }
            }
            getUserNotificationsUseCase().collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        updateState {
                            copy(
                                notifications = resource.data.map { it.toPresentation() },
                                isRefreshing = false
                            )
                        }
                        markAllAsRead()
                    }

                    is Resource.Error -> {
                        emitEffect(effect = NotificationEffect.ShowErrorMessage(errorMessageRes = resource.error.toMessageRes()))
                        updateState {
                            copy(
                                errorMessageRes = resource.error.toMessageRes(),
                                isRefreshing = false
                            )
                        }
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