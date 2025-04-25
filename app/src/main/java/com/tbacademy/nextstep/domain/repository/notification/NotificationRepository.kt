package com.tbacademy.nextstep.domain.repository.notification

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun listenForUnreadNotifications(): Flow<Boolean>
    suspend fun markAllAsRead()
}