package com.tbacademy.nextstep.domain.repository.notification

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun listenForUnreadNotifications(): Flow<Boolean>
    fun getUserNotifications(): Flow<Resource<List<Notification>>>
    suspend fun markAllAsRead()
}