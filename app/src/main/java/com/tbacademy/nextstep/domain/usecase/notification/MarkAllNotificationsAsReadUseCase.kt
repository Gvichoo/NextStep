package com.tbacademy.nextstep.domain.usecase.notification

import com.tbacademy.nextstep.domain.repository.notification.NotificationRepository
import javax.inject.Inject

interface MarkAllNotificationsAsReadUseCase {
    suspend operator fun invoke()
}

class MarkAllNotificationsAsReadUseCaseImpl @Inject constructor(
    private val notificationRepository: NotificationRepository
) : MarkAllNotificationsAsReadUseCase {
    override suspend fun invoke() = notificationRepository.markAllAsRead()
}