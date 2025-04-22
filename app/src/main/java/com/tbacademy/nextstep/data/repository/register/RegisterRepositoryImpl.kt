package com.tbacademy.nextstep.data.repository.register

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.remote.dto.UserDto
import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.register.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : RegisterRepository {

    override fun register(nickname: String, email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(true))

        try {
            val usernameQuery = fireStore.collection("users")
                .whereEqualTo("username", nickname)
                .get()
                .await()

            if (!usernameQuery.isEmpty) {
                emit(Resource.Error(error = ApiError.UserAlreadyExists))
                return@flow
            }

            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid

            if (uid != null) {
                val user = UserDto(uid = uid, email = email, username = nickname)

                fireStore.collection("users").document(uid).set(user)

                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(ApiError.ServiceUnavailable))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
        } finally {
            emit(Resource.Loading(loading = false))
        }
    }
}



