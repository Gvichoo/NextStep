package com.tbacademy.nextstep.data.repository.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.remote.dto.UserDto
import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.User
import com.tbacademy.nextstep.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UserRepository {
    override fun getUserInfo(userId: String): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading(loading = true))

            try {
                val userSnapshot = firestore.collection(USER_COLLECTION_KEY)
                    .document(userId)
                    .get()
                    .await()
                val userDto = userSnapshot.toObject(UserDto::class.java)

                if (userDto != null) {
                    emit(Resource.Success(data = userDto.toDomain()))
                } else {
                    emit(Resource.Error(error = ApiError.NotFound))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.toApiError()))
            } finally {
                emit(Resource.Loading(loading = false))
            }
        }
    }

    private companion object {
        const val USER_COLLECTION_KEY = "users"
    }
}