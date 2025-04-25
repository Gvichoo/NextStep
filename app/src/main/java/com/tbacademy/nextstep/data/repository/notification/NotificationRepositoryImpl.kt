package com.tbacademy.nextstep.data.repository.notification

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.data.remote.dto.NotificationDto
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import com.tbacademy.nextstep.domain.model.Notification
import com.tbacademy.nextstep.domain.repository.notification.NotificationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val authManager: AuthManager,
    private val firestore: FirebaseFirestore,
    private val handleResponse: HandleResponse
) : NotificationRepository {

    override fun listenForUnreadNotifications(): Flow<Boolean> = callbackFlow {
        val userId = authManager.getCurrentUserId()
        if (userId == null) {
            trySend(false)
            close()
            return@callbackFlow
        }

        val registration = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(NOTIFICATIONS_COLLECTION)
            .whereEqualTo(FIELD_IS_READ, false)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(false)
                    return@addSnapshotListener
                }
                trySendBlocking(snapshot?.isEmpty == false)
            }

        awaitClose { registration.remove() }
    }

    override fun getUserNotifications(): Flow<Resource<List<Notification>>> {
        return handleResponse.safeApiCallWithUserId { userId ->
            val snapshot = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(NOTIFICATIONS_COLLECTION)
                .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(NotificationDto::class.java)?.copy(id = doc.id)
            }.map { it.toDomain() }
        }
    }

    override suspend fun markAllAsRead() {
        val userId = authManager.getCurrentUserId() ?: return

        val snapshot = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(NOTIFICATIONS_COLLECTION)
            .whereEqualTo(FIELD_IS_READ, false)
            .get()
            .await()

        val batch = firestore.batch()
        snapshot.documents.forEach { doc ->
            batch.update(doc.reference, FIELD_IS_READ, true)
        }
        batch.commit().await()
    }

    private companion object {
        const val USERS_COLLECTION = "users"
        const val NOTIFICATIONS_COLLECTION = "notifications"
        const val FIELD_IS_READ = "isRead"
        const val FIELD_CREATED_AT = "createdAt"
    }
}