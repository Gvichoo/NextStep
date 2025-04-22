package com.tbacademy.nextstep.data.repository.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.FirebaseHelper
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
    private val firebaseHelper: FirebaseHelper
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

    override fun searchUsers(query: String): Flow<Resource<List<User>>> {
        return firebaseHelper.safeFlowResource {
            val usersSnapshot = firestore.collection(USER_COLLECTION_KEY)
                .orderBy("usernameLower")
                .startAt(query.lowercase())
                .endAt(query.lowercase() + "\uf8ff")
                .get()
                .await()

            val userDtoList = usersSnapshot.documents.mapNotNull {
                it.toObject(UserDto::class.java)?.copy(uid = it.id)
            }

            userDtoList.map { it.toDomain() }
        }
    }

    private companion object {
        const val USER_COLLECTION_KEY = "users"
    }
}