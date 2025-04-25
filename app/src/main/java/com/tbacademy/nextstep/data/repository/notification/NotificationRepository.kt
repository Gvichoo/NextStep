package com.tbacademy.nextstep.data.repository.notification

import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import com.tbacademy.nextstep.domain.repository.notification.NotificationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val authManager: AuthManager,
    private val firestore: FirebaseFirestore
) : NotificationRepository {

    override fun listenForUnreadNotifications(): Flow<Boolean> = callbackFlow {
        val userId = authManager.getCurrentUserId()
        if (userId == null) {
            trySend(false)
            close()
            return@callbackFlow
        }

        val registration = firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .whereEqualTo("isRead", false)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(false)
                    return@addSnapshotListener
                }
                trySendBlocking(snapshot?.isEmpty == false)
            }

        awaitClose { registration.remove() }
    }


    override suspend fun markAllAsRead() {
        val userId = authManager.getCurrentUserId() ?: return

        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .whereEqualTo("isRead", false)
            .get()
            .await()

        val batch = firestore.batch()
        snapshot.documents.forEach { doc ->
            batch.update(doc.reference, "isRead", true)
        }
        batch.commit().await()
    }
}