package com.tbacademy.nextstep.data.httpHelper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.core.ApiError
import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HandleResponse @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authManager: AuthManager,
    private val firestore: FirebaseFirestore
) {
    fun <T> safeApiCallWithUserId(
        action: suspend (userId: String) -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading(true))
        val userId = authManager.getCurrentUserId()
        if (userId == null) {
            emit(Resource.Error(ApiError.Unauthorized))
        } else {
            try {
                val result = action(userId)
                emit(Resource.Success(result))
            } catch (e: Exception) {
                emit(Resource.Error(e.toApiError()))
            }
        }
        emit(Resource.Loading(false))
    }

    fun <T> safeApiCall(
        action: suspend () -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading(true))
        try {
            val result = action()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
        }
        emit(Resource.Loading(false))
    }


    fun <T> withUserSnapshotFlow(
        action: suspend (userId: String, userSnapshot: DocumentSnapshot) -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading(true))
        val user = firebaseAuth.currentUser
        if (user == null) {
            emit(Resource.Error(ApiError.Unauthorized))
            return@flow
        }

        try {
            val snapshot = firestore.collection("users").document(user.uid).get().await()

            if (!snapshot.exists()) {
                emit(Resource.Error(ApiError.NotFound))
                return@flow
            }

            val result = action(user.uid, snapshot)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
        } finally {
            emit(Resource.Loading(false))
        }
    }

    companion object {
        const val SORT_CREATED_AT = "createdAt"
    }
}