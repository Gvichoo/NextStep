package com.tbacademy.nextstep.domain.usecase.notification

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.model.Notification
import com.tbacademy.nextstep.domain.repository.notification.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetUserNotificationsUseCase {
    suspend operator fun invoke(): Flow<Resource<List<Notification>>>
}

class GetUserNotificationsUseCaseImpl @Inject constructor(
    private val notificationRepository: NotificationRepository
) : GetUserNotificationsUseCase {
    override suspend fun invoke(): Flow<Resource<List<Notification>>> {
        return notificationRepository.getUserNotifications()
    }
}