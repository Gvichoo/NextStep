package com.tbacademy.nextstep.domain.usecase.notification

import com.tbacademy.nextstep.domain.repository.notification.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ListenForUnreadNotificationsUseCase {
    operator fun invoke(): Flow<Boolean>
}

class ListenForUnreadNotificationsUseCaseImpl @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ListenForUnreadNotificationsUseCase {
    override fun invoke(): Flow<Boolean> = notificationRepository.listenForUnreadNotifications()
}